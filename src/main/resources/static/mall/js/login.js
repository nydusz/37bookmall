function login() {
    var loginName = $("#loginName").val();
    if (!validPhoneNumber(loginName)) {
        swal('请输入正确的登录名(即手机号)', {
            icon: "error",
        });
        return false;
    }
    var password = $("#password").val();
    if (!validPassword(password)) {
        swal('请输入正确的密码格式(6-20位字符和数字组合)', {
            icon: "error",
        });
        return false;
    }
    var verifyCode = $("#verifyCode").val();
    if (!validLength(verifyCode, 7)) {
        swal('请输入正确的验证码', {
            icon: "error",
        });
        return false;
    }
    //验证
    var params = $("#loginForm").serialize();               //输出表单结果
    var url = '/login';
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        data: params,
        success: function (result) {
            if (result.resultCode == 200) {
                window.location.href = '/index';
            } else {
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
}