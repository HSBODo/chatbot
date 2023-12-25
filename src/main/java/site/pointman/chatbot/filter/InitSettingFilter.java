package site.pointman.chatbot.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
    }

    @Override
    public void destroy() {
        log.info("==== 필터 인스턴스 종료 ====");
    }
}
