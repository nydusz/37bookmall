$(document).ready(function () {
    $(".top_btn").hide();
    $(".cus_serve").hide();
    $(".cus_love").hide();
    $(function () {
        $(window).scroll(function () {
            if ($(window).scrollTop() > 100) {
                $(".top_btn").fadeIn(500);
                $(".cus_serve").fadeIn(500);
                $(".cus_love").fadeIn(500);
            } else {
                $(".top_btn").fadeOut(500);
                $(".cus_serve").fadeOut(500);
                $(".cus_love").fadeOut(500);
            }
        });
        //当点击跳转链接后，回到页面顶部位置
        $(".top_btn").click(function () {
            $('body,html').animate({
                    scrollTop: 0
                },
                1000);
            return false;
        });
    });
});