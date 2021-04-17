
    function cancelOrder() {
        var orderNo = $("#orderNoValue").val();
        swal({
            title: "客官请注意",
            text: "确认要取消订单吗?",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        }).then((flag) => {
                if (flag) {
                    $.ajax({
                        type: 'PUT',
                        url: '/orders/' + orderNo + '/cancel',
                        success: function (result) {
                            if (result.resultCode == 200) {
                                window.location.reload();
                            } else {
                                swal(result.message, {
                                    icon: "error",
                                });
                            }
                        },
                        error: function () {
                            swal("操作失败", {
                                icon: "error",
                            });
                        }
                    });
                }
            }
        )
        ;
    }

    function payOrder() {
        var orderNo = $("#orderNoValue").val();
        window.location.href = '/selectPayType?orderNo=' + orderNo;
    }

    function finishOrder() {
        var orderNo = $("#orderNoValue").val();
        $.ajax({
            type: 'PUT',
            url: '/orders/' + orderNo + '/finish',
            success: function (result) {
                if (result.resultCode == 200) {
                    window.location.reload();
                } else {
                    swal(result.message, {
                        icon: "error",
                    });
                }
            },
            error: function () {
                swal("操作失败", {
                    icon: "error",
                });
            }
        });
    }
