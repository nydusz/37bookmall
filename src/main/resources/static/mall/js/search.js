var cspath = getMyPath();
var jsN = finds(cspath, '/', 2); //识别/第三次出现位置
function finds(str, cha, num) {
    var x = str.indexOf(cha);
    for (var i = 0; i < num; i++) {
        x = str.indexOf(cha, x + 1);
    }
    return x;
}
if (cspath.indexOf("http:") > -1 || cspath.indexOf("https:") > -1)
{
var cspath = cspath.substr(jsN); //最终js路径
//console.log(cspath);
}
function getcsUrl(t,o) {
    $("head").append("<link>");
    css = $("head").children(":last");
    if (o == 1) {
        css.attr({
            rel: "stylesheet",
            type: "text/css",
            href:  t + 'css1.css'
        });
    } else if (o == 2) {
        css.attr({
            rel: "stylesheet",
            type: "text/css",
            href: t + 'css2.css'
        });
    } else if (o == 3) {
        css.attr({
            rel: "stylesheet",
            type: "text/css",
            href:  t + 'css3.css'
        });
    } else if (o == 4) {
        css.attr({
            rel: "stylesheet",
            type: "text/css",
            href:  t + 'css4.css'
        });
    } else if (o == 5) {
        css.attr({
            rel: "stylesheet",
            type: "text/css",
            href:  t + 'css5.css'
        });
    }
}

//分页属性数组
var Objarr = [];
//分页控件索引
var ObjControlIndex = {
    index: 0
};

(function () {
    var dmm = function (options1) {
        $("#" + options1.container + "").html("");
        var page = "<div class='w-btn-group x-pages-f' id='" + options1.container + "_page'></div>";
        var options = $.extend({
            index: 1,
            container: "",
            callback: function () {
            },
            size: [10, 20]//页面大小
            , skip: false,
            cssStyle:1,
            setSize: false,
            textShow: ["首页", "上一页", "下一页", "尾页", "跳转","页","确定","页数：", "总条数："]
        }, options1);
        getcsUrl(cspath, options.cssStyle);
        var Obj1 = {
            index: options.index,
            countpage: 1,
            infocount: 1,
            pageSize1: options.size[0],
            isSkip: false,
            huidiao: options.callback,
            sizearr: options.size,
            container: options.container,
            textShow: options.textShow,
            controlIndex: ObjControlIndex.index
        };
        if (options.skip != undefined) {
            Obj1.isSkip = options.skip;
        }
        Objarr.push(Obj1);
        var sele = "<div class='selPa'><select class='w-select' id='" + options.container + "_w-select' onchange='changess(" + Obj1.controlIndex + ")'></div>";
        $("#" + options.container + "").append(page);
        hui(Obj1);

        for (var o = 0; o < Obj1.sizearr.length; o++) {
            sele += "<option value='" + Obj1.sizearr[o] + "' >" + Obj1.sizearr[o] + "</option>";
        }
        sele += "</select>";
        if (options.setSize)
        {
            $("#" + options.container + "").append(sele);
        }
        ObjControlIndex.index++;
    };
    window.dmm = dmm;
})();

function hui(obj) {
    
    var options1 = $.extend(Objarr[obj.controlIndex], obj);
    Objarr[obj.controlIndex].huidiao(options1);
}

function xian(obj) {
    $("#" + obj.container + "_page.w-btn-group").html("");

    var shoustr = "<button id='" + obj.container + "_btnFirst' type='button' class='button gray firstPage' onclick='page(" + JSON.stringify(obj.controlIndex) + ",1)'>" + obj.textShow[0]+"</button>" +
        "<button id='" + obj.container + "_btnPre' type='button' class='button gray' onclick='page(" + JSON.stringify(obj.controlIndex) + ",2)'>" + obj.textShow[1] +"</button>" +
        "<button id='" + obj.container + "_btnLas' type='button' class='button gray' onclick='page(" + JSON.stringify(obj.controlIndex) + ",3)'>" + obj.textShow[2] +"</button>";
    if (obj.isSkip) {
        shoustr += "<button id='" + obj.container + "_btnEnd' type='button' class='button gray epage lastPage' onclick='page(" + JSON.stringify(obj.controlIndex) + ",4)'>" + obj.textShow[3] + "</button>&nbsp;&nbsp;" + obj.textShow[4] + "<input type='text' id='" + obj.container + "_toindex' class='jumps'/>" + obj.textShow[5] + "&nbsp;&nbsp;<button type='button' onclick='rediretoheadindex(" + JSON.stringify(obj.controlIndex) + ")' class='button gray imsury'>" + obj.textShow[6] + "</button><span class='w-show'>" + obj.textShow[7]  + (obj.infocount == 0 ? 0 : obj.index) + " / " + obj.countpage + " &nbsp;&nbsp;&nbsp;" + obj.textShow[8] + obj.infocount + "</span>";
    } else {
        shoustr += "<button id='" + obj.container + "_btnEnd' type='button' class='button gray lastPage' onclick='page(" + JSON.stringify(obj.controlIndex) + ",4)'>" + obj.textShow[3] + "</button>&nbsp;&nbsp;" + obj.textShow[7]  + (obj.infocount == 0 ? 0 : obj.index) + " / " + obj.countpage + " &nbsp;&nbsp;&nbsp;" + obj.textShow[8]  + obj.infocount + "</span>";
    }

    $("#" + obj.container + "_page.w-btn-group").append(shoustr);
    if (obj.countpage < 6 || obj.index >= obj.countpage - 4) {
        for (var i = obj.countpage, j = 0; j < 5 && i > 0; i--, j++) {
            var str = "<button id='" + obj.container + "_child" + i + "' type='button' class='button gray' onclick='redire(" + JSON.stringify(obj.controlIndex) + "," + i + ")'>" + i + "</button>";
            $("#" + obj.container + "_page.w-btn-group").children().eq(1).after(str);
        }
    }
    else if (obj.index < obj.countpage - 4 & obj.countpage >= 6) {
        for (var i = obj.countpage, j = 0; j < 5; j++) {
            if (j == 1) {
                var str = str = "<button id='" + obj.container + "_child" + i + "' type='button' class='button gray'>┄</button>";
                $("#" + obj.container + "_page.w-btn-group").children().eq(1).after(str);
                i = obj.index + 2;
            }
            else {
                var str = str = "<button id='" + obj.container + "_child" + i + "' type='button' class='button gray' onclick='redire(" + JSON.stringify(obj.controlIndex) + "," + i + ")'>" + i + "</button>";
                $("#" + obj.container + "_page.w-btn-group").children().eq(1).after(str);
                i--;
            }
        }
    }
    $("#" + obj.container + "_child" + obj.index).addClass("active");
    $("#" + obj.container + "_page").children().not("#" + obj.container + "_child" + obj.index).each(function () {
        $(this).removeClass("active");
    });
    ceshi(obj);
}

//下拉框改变
function changess(controlIndex) {
    Objarr[controlIndex].index = 1;
    Objarr[controlIndex].pageSize1 = $('#' + Objarr[controlIndex].container + '_w-select').val();
    Objarr[controlIndex].huidiao(Objarr[controlIndex]);
}
//页数按钮
function redire(controlIndex, o) {
    Objarr[controlIndex].index = o;
    //跳转页面
    hui(Objarr[controlIndex]);
}
//跳转页面
function rediretoheadindex(controlIndex) {
    var obj = Objarr[controlIndex];
    if ($("#" + obj.container + "_toindex").val() == "" || $("#" + obj.container + "_toindex").val() == "") {
        return;
    }
    var reg = /^[0-9]*$/;
    if (!reg.test($("#" + obj.container + "_toindex").val())) {
        alert("请输入数字");
        return;
    }

    if ($("#" + obj.container + "_toindex").val() > obj.countpage) {
        alert("不能大于总页数");
        return;
    }
    if ($("#" + obj.container + "_toindex").val() < 1) {
        alert("不能小于1");
        return;
    }
    obj.index = Number($("#" + obj.container + "_toindex").val());
    hui(obj);
}

function page(controlIndex, o) {
    var obj = Objarr[controlIndex];
    if (o == 1) {
        $("#" + obj.container + "_btnLas").attr("disabled", "false");
        obj.index = 1;
    }
    if (o == 2) {
        $("#" + obj.container + "_btnLas").attr("disabled", "false");
        if (obj.index > 1) {
            obj.index--;
        }
        else {
            $("#" + obj.container + "_btnPre").attr("disabled", "true");
            $("#" + obj.container + "_btnPre").css({"color": "#ddd","cursor":"not-allowed"});
        }
    }
    if (o == 3) {
        $("#" + obj.container + "_btnPre").attr("disabled", "false");
        if (obj.index < obj.countpage) {
            obj.index++;
        }
        else {
            $("#" + obj.container + "_btnLas").attr("disabled", "true");
            $("#" + obj.container + "_btnLas").css({"color":"#ddd","cursor": "not-allowed"});
        }
    }
    if (o == 4) {
        $("#" + obj.container + "_btnPre").attr("disabled", "false");
        obj.index = obj.countpage;
    }
    hui(obj);
}

function ceshi(obj) {
    if (obj.index == obj.countpage) {
        $("#" + obj.container + "_btnLas").attr("disabled", "true");
        $("#" + obj.container + "_btnLas").css({"color": "#ddd","cursor":"not-allowed"});
        $("#" + obj.container + "_btnEnd").attr("disabled", "true");
        $("#" + obj.container + "_btnEnd").css({ "color": "#ddd", "cursor": "not-allowed" });
    }
    if (obj.index == 1) {
        $("#" + obj.container + "_btnPre").attr("disabled", "true");
        $("#" + obj.container + "_btnPre").css({ "color": "#ddd", 'cursor': 'not-allowed' });
        $("#" + obj.container + "_btnFirst").attr("disabled", "true");
        $("#" + obj.container + "_btnFirst").css({"color":"#ddd","cursor" :"not-allowed"});
    }
    if(obj.countpage<1)
    {
        $("#" + obj.container + "_btnLas").attr("disabled", "true");
        $("#" + obj.container + "_btnLas").css({ "color": "#ddd", "cursor": "not-allowed" });
        $("#" + obj.container + "_btnEnd").attr("disabled", "true");
        $("#" + obj.container + "_btnEnd").css({ "color": "#ddd", "cursor": "not-allowed" });
    }
}





function getMyPath() {
    var scriptSrc = document.getElementsByTagName('script')[document.getElementsByTagName('script').length - 1].src;
    var jsName = scriptSrc.split('/')[scriptSrc.split('/').length - 1];
    return scriptSrc.replace(jsName, '');
}



$(function () {
    $('#keyword').keypress(function (e) {
        var key = e.which; 
        if (key == 13) {
            var q = $(this).val();
            if (q && q != '') {
                window.location.href = '/search?keyword=' + q;
            }
        }
    });
});

var none =$("a[href^='###']");
$(none).css("color", "#ddd");

function search() {
    var q = $('#keyword').val();
    if (q && q != '') {
        window.location.href = '/search?keyword=' + q;
    }
}