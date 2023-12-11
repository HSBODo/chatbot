package site.pointman.chatbot.Interceptor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;
import site.pointman.chatbot.dto.request.RequestDto;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    //	Controller 진입 전 실행
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");

        return true;
//
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        ResponseDto responseDto = new ResponseDto();
//        responseDto.addSimpleText("인증실패");
//        String responseBody = objectMapper.writeValueAsString(responseDto);
//        response.getWriter().write(responseBody);
    }

    private String getResponseBody(ContentCachingResponseWrapper responseWrapper) {
        return new String(responseWrapper.getContentAsByteArray());
    }

    private ContentCachingResponseWrapper getResponseWrapper(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper)response;
        }
        return new ContentCachingResponseWrapper(response);
    }

    private RequestDto getRequestDtoFromHttpServletRequest(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        RequestDto requestDto = objectMapper.readValue(messageBody, RequestDto.class);
        return requestDto;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }
}
