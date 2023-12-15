package site.pointman.chatbot.Interceptor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;
import site.pointman.chatbot.domain.request.ChatBotRequest;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
public class Interceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    //	Controller 진입 전 실행
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("인터셉터-preHandle");
        return true;
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

    private ChatBotRequest getRequestDtoFromHttpServletRequest(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        ChatBotRequest chatBotRequest = objectMapper.readValue(messageBody, ChatBotRequest.class);
        return chatBotRequest;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("인터셉터-postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("인터셉터-afterCompletion");
    }
}
