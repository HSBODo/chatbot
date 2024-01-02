package site.pointman.chatbot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.pointman.chatbot.Interceptor.Interceptor;
import site.pointman.chatbot.filter.InitSettingFilter;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.serviceimpl.AuthServiceImpl;

@Configuration
public class InterceptorsConfig implements WebMvcConfigurer {

    AuthService authService;

    public InterceptorsConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor(authService))
                .excludePathPatterns("/css/**", "/images/**", "/js/**","/kakaochatbot/**","/login")
        ;
//        addPathPatterns		Interceptor 적용 대상
//        excludePathPatterns		Interceptor 제외 대상
    }
}
