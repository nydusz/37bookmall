   /**
     * 添加到购物车
     */
    function saveToCart(id) {
        var goodsCount = 1;
        var data = {
            "goodsId": id,
            "goodsCount": goodsCount
        };
        $.ajax({
            type: 'POST',
            url: '/shop-cart',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (result) {
                if (result.resultCode == 200) {
                    swal({
                        title: "添加成功",
                        text: "确认框",
                        icon: "success",
                        buttons: true,
                        dangerMode: true,
                    }).then((flag) => {
                            window.location.reload();
                        }
                    );
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

    /**
     * 添加到购物车并跳转至购物车页面
     */
    function saveAndGoCart(id) {
        var goodsCount = 1;
        var data = {
            "goodsId": id,
            "goodsCount": goodsCount
        };
        $.ajax({
            type: 'POST',
            url: '/shop-cart',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (result) {
                if (result.resultCode == 200) {
                    swal({
                        title: "已将商品加入购物车",
                        icon: "success",
                        buttons: {
                            cancel: "留在当前页",
                            confirm: "去购物车结算"
                        },
                        dangerMode: false,
                    }).then((flag) => {
                            if (flag) {
                                window.location.href = '/shop-cart';
                            }
                        }
                    );
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