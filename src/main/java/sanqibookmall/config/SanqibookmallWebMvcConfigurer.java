package sanqibookmall.config;

import sanqibookmall.common.Constants;
import sanqibookmall.interceptor.AdminLoginInterceptor;
import sanqibookmall.interceptor.SanqiCartNumberInterceptor;
import sanqibookmall.interceptor.SanqiLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringMvc拓展
 * 后台登录拦截
 *用户登录拦截
 */
@Configuration
public class SanqibookmallWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;
    @Autowired
    private SanqiLoginInterceptor sanqiLoginInterceptor;
    @Autowired
    private SanqiCartNumberInterceptor sanqiCartNumberInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，拦截以/admin为前缀的url路径（后台登陆拦截）
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/admin/**")   //拦截/admin下的任意请求（所有请求）
                .excludePathPatterns("/admin/login")    //除去登录页面
                .excludePathPatterns("/admin/dist/**")  //静态资源的登录样式
                .excludePathPatterns("/admin/plugins/**");
        // 购物车中的数量统一处理
        registry.addInterceptor(sanqiCartNumberInterceptor)
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("/login")
                .excludePathPatterns("/logout");
        // 用户登陆拦截
        registry.addInterceptor(sanqiLoginInterceptor)
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("/login")
                .excludePathPatterns("/logout")
                .addPathPatterns("/goods/detail/**")
                .addPathPatterns("/shop-cart")
                .addPathPatterns("/shop-cart/**")
                .addPathPatterns("/saveOrder")
                .addPathPatterns("/orders")
                .addPathPatterns("/orders/**")            
                .addPathPatterns("/personal")
                .addPathPatterns("/personal/updateInfo")
                .addPathPatterns("/selectPayType")
                .addPathPatterns("/payPage")
                .addPathPatterns("/recommend");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
        registry.addResourceHandler("/goods-img/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }
}
