package site.pointman.chatbot.handler.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class InitSettingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // 전처리
        log.info("==== 1.필터 스타트 ====");
        log.info("request Url = {}", httpServletRequest.getRequestURL());
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("==== 필터 인스턴스 초기화 ====");
        try {
            File profile = new File("image/profile");
            Files.createDirectories(profile.toPath());
            log.info("폴더생성:{}",profile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        log.info("==== 필터 인스턴스 종료 ====");
    }
}
