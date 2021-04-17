$(function () {
    /*
        url：提交处理数据的地址。
        datatype：这个参数用于设定将要得到的数据类型。我最常用的是“json”，其余的类型还包括：xml、xmlstring、local、javascript、function。
        mtype: 定义使用哪种方法发起请求，GET或者POST。
        height：Grid的高度，可以接受数字、%值、auto，默认值为150。
        width：Grid的宽度，如果未设置，则宽度应为所有列宽的之和；如果设置了宽度，则每列的宽度将会根据shrinkToFit选项的设置，进行设置。
        shrinkToFit：此选项用于根据width计算每列宽度的算法。默认值为true。如果shrinkToFit为true且设置了width值，则每列宽度会根据width成比例缩放；如果shrinkToFit为false且设置了width值，则每列的宽度不会成比例缩放，而是保持原有设置，而Grid将会有水平滚动条。
        autowidth：默认值为false。如果设为true，则Grid的宽度会根据父容器的宽度自动重算。重算仅发生在Grid初始化的阶段；如果当父容器尺寸变化了，同时也需要变化Grid的尺寸的话，则需要在自己的代码中调用setGridWidth方法来完成。
        pager：定义页码控制条PageBar
        sortname：指定默认的排序列，可以是列名也可以是数字。此参数会在被传递到服务端。
        viewrecords：设置是否在PagerBar显示所有记录的总数。
        caption：Grid的标题。如果设置了，则将显示在Grid的Header层 ；如果未设置，则标题区域不显示 。
        rowNum：用于设置Grid中一次显示的行数，默认值为20。
        rowList：一个数组，用于设置Grid可以接受的rowNum值。例如[10,20,30]。
        prmNames：这是一个数组，用于设置jqGrid将要向服务端传递的参数名称。我们一般不用去改变什么。
        colModel：最重要的数组之一，用于设定各列的参数。（稍后详述）
        jsonReader：这又是一个数组，用来设定如何解析从Server端发回来的json数据。


        name：为Grid中的每个列设置唯一的名称，这是一个必需选项，其中保留字包括subgrid、cb、rn。
        index：设置排序时所使用的索引名称，这个index名称会作为sidx参数传递到服务端。
        label：表格显示的列名。
        width：设置列的宽度，目前只能接受以px为单位的数值，默认为150。
        sortable：设置该列是否可以排序，默认为true。
        search：设置该列是否可以被列为搜索条件，默认为true。
        resizable：设置列是否可以变更尺寸，默认为true。
        hidden：设置此列初始化时是否为隐藏状态，默认为false。
        formatter：预设类型或用来格式化该列的自定义函数名。常用预设格式有：integer、date、currency、number等
    */

    $("#jqGrid").jqGrid({
        url: '/admin/carousels/list',           // 加载数据时访问的url
        datatype: "json",                       //返回的数据类型，后台需要返回一个json字符串
        colModel: [
            {label: 'id', name: 'carouselId', index: 'carouselId', width: 50, key: true, hidden: true},
            {label: '轮播图', name: 'carouselUrl', index: 'carouselUrl', width: 180, formatter: coverImageFormatter},
            {label: '跳转链接', name: 'redirectUrl', index: 'redirectUrl', width: 120},
            {label: '排序值', name: 'carouselRank', index: 'carouselRank', width: 120},
            {label: '添加时间', name: 'createTime', index: 'createTime', width: 120}
        ],      // 表格表头
        height: 560,        //设置表格高度
        rowNum: 10,         //设置显示记录条数，默认20，若返回的记录条数大于rowNum,grid只会显示rowNum规定的条数
        rowList: [10, 20, 50],          //用与设置grid能够接受的rowNum值
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    function coverImageFormatter(cellvalue) {
        return "<img src='" + cellvalue + "' height=\"120\" width=\"160\" alt='coverImage'/>";
    }

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    new AjaxUpload('#uploadCarouselImage', {
        action: '/admin/upload/file',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                alert('只支持jpg、png、gif格式的文件！');
                return false;
            }
        },
        onComplete: function (file, r) {
            if (r != null && r.resultCode == 200) {
                $("#carouselImg").attr("src", r.data);
                $("#carouselImg").attr("style", "width: 128px;height: 128px;display:block;");
                return false;
            } else {
                alert("error");
            }
        }
    });
});

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function carouselAdd() {
    reset();
    $('.modal-title').html('轮播图添加');
    $('#carouselModal').modal('show');
}

//绑定modal上的保存按钮
$('#saveButton').click(function () {
    var redirectUrl = $("#redirectUrl").val();
    var carouselRank = $("#carouselRank").val();
    var carouselUrl = $('#carouselImg')[0].src;
    var data = {
        "carouselUrl": carouselUrl,
        "carouselRank": carouselRank,
        "redirectUrl": redirectUrl
    };
    var url = '/admin/carousels/save';
    var id = getSelectedRowWithoutAlert();
    if (id != null) {
        url = '/admin/carousels/update';
        data = {
            "carouselId": id,
            "carouselUrl": carouselUrl,
            "carouselRank": carouselRank,
            "redirectUrl": redirectUrl
        };
    }
    $.ajax({
        type: 'POST',
        url: url,
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
            if (result.resultCode == 200) {
                $('#carouselModal').modal('hide');
                swal("保存成功", {
                    icon: "success",
                });
                reload();
            } else {
                $('#carouselModal').modal('hide');
                swal(result.message, {
                    icon: "error",
                });
            }
            ;
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});

function carouselEdit() {
    reset();
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    //请求数据
    $.get("/admin/carousels/info/" + id, function (r) {
        if (r.resultCode == 200 && r.data != null) {
            //填充数据至modal
            $("#carouselImg").attr("src", r.data.carouselUrl);
            $("#carouselImg").attr("style", "height: 64px;width: 64px;display:block;");
            $("#redirectUrl").val(r.data.redirectUrl);
            $("#carouselRank").val(r.data.carouselRank);
        }
    });
    $('.modal-title').html('轮播图编辑');
    $('#carouselModal').modal('show');
}

function deleteCarousel() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要删除数据吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: "/admin/carousels/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.resultCode == 200) {
                            swal("删除成功", {
                                icon: "success",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            swal(r.message, {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    )
    ;
}


function reset() {
    $("#redirectUrl").val('##');
    $("#carouselRank").val(0);
    $("#carouselImg").attr("src", '/admin/dist/img/img-upload.png');
    $("#carouselImg").attr("style", "height: 64px;width: 64px;display:block;");
    $('#edit-error-msg').css("display", "none");
}