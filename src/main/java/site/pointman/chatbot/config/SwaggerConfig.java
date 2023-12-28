package site.pointman.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
//@EnableWebMvc
@Configuration
public class SwaggerConfig {

    private static final String SERVICE_NAME = "중계나라";
    private static final String API_VERSION = "V1";
    private static final String API_DESCRIPTION = "중계나라 관리자 기능 API 명세서";
    private static final String API_URL = "http://localhost:8080/";



    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("site.pointman.chatbot.controller.admin"))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(SERVICE_NAME)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .build();
    }


}
