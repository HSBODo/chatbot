package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.notice.NoticeDto;
import site.pointman.chatbot.dto.notice.NoticeListDto;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.utill.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class AdminServiceImplTest {
    HttpUtils httpUtils;

    @Autowired
    AuthService authService;
    @Test
    void getNotice() {
        OAuthTokenDto token = authService.createToken();
        String url = "https://api.commerce.naver.com/external/v1/contents/seller-notices?page=1&size=10";

        Map<String,Object> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+token.getAccessToken());
        headers.put("Content-type","application/json");

        String httpResponse = HttpUtils.get(url, headers);

        log.info("httpResponse = {}",httpResponse);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            NoticeListDto noticeListDto = mapper.readValue(httpResponse, NoticeListDto.class);

            for(int i = 0 ; i<noticeListDto.getNoticeListDto().size(); i ++){
                NoticeDto noticeDto = noticeListDto.getNoticeListDto().get(i);
                String sellerNoticeId = noticeDto.getSellerNoticeId();
                url = "https://api.commerce.naver.com/external/v1/contents/seller-notices/"+sellerNoticeId;
                httpResponse = HttpUtils.get(url, headers);
                log.info("httpResponse = {}",httpResponse);
            }




        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}