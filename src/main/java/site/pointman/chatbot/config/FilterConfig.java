package site.pointman.chatbot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.filter.InitSettingFilter;

@Slf4j
@Configuration
public class FilterConfig {
    @Value("${spring.datasource.url}")
    private String DATASOURCE_URL;

    @Bean
    public FilterRegistrationBean filterBean() {
        log.info("datasource={}",DATASOURCE_URL);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new InitSettingFilter());
        registrationBean.addUrlPatterns("/*"); //전체 URL 포함
//        registrationBean.addUrlPatterns("/test/*"); //특정 URL 포함
//        registrationBean.setUrlPatterns(Arrays.asList(INCLUDE_PATHS)); //여러 특정 URL 포함
        return registrationBean;
    }
}
