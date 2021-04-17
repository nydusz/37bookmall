$('#saveOrder').click(function () {
    var userAddress = $(".user_address_label").html();
    if (userAddress == '' || userAddress == '无') {
        swal("请填写收货信息", {
            icon: "error",
        });
        return;
    }
    if (userAddress.trim().length < 10) {
        swal("请输入正确的收货信息", {
            icon: "error",
        });
        return;
    }
    window.location.href = '../saveOrder';
});

function openUpdateModal() {
    $('#personalInfoModal').modal('show');
}

//绑定modal上的保存按钮
$('#saveButton').click(function () {
    var address = $("#address").val();
    var userId = $("#userId").val();
    var data = {
        "userId": userId,
        "address": address
    };
    $.ajax({
        type: 'POST',//方法类型
        url: '/personal/updateInfo',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
            if (result.resultCode == 200) {
                $('#personalInfoModal').modal('hide');
                window.location.reload();
            } else {
                $('#personalInfoModal').modal('hide');
                swal(result.message, {
                    icon: "error",
                });
            }
            ;
        },
        error: function () {
            swal('操作失败', {
                icon: "error",
            });
        }
    });
});