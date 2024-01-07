package site.pointman.chatbot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.pointman.chatbot.handler.Interceptor.Interceptor;
import site.pointman.chatbot.globalservice.AuthService;

@Configuration
public class InterceptorsConfig implements WebMvcConfigurer {

    AuthService authService;

    public InterceptorsConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor(authService))
                .excludePathPatterns("/css/**", "/images/**", "/js/**","/kakaochatbot/**","/login","/order/**")
        ;
//        addPathPatterns		Interceptor 적용 대상
//        excludePathPatterns		Interceptor 제외 대상
    }
}
