package site.pointman.chatbot.handler.Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.globalservice.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class Interceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper = new ObjectMapper();
    AuthService authService;

    public Interceptor(AuthService authService) {
        this.authService = authService;
    }


    @Override
    //	Controller 진입 전 실행
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("==== 2.인터셉터-preHandle ====");

        String token = request.getHeader("token");

        if(!authService.isTokenVerification(token)) {

            Response forbiddenResponse = new Response(ResultCode.FAIL,"토큰이 유효하지 않습니다.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            String forbiddenResponseAsString = objectMapper.writeValueAsString(forbiddenResponse);
            response.getWriter().write(forbiddenResponseAsString);

            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("5.인터셉터-postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("6.인터셉터-afterCompletion");
    }
}
