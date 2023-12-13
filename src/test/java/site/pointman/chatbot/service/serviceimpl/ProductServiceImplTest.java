package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.utill.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class ProductServiceImplTest {
    @Autowired
    AuthService authService;

    @Autowired
    ProductService productService;

    HttpUtils httpUtils;

    @Test
    void getProduct() {
        OAuthTokenDto token = authService.createToken();
        MediaType mediaType = MediaType.parse("application/json");
        String url = "https://api.commerce.naver.com/external/v2/products/origin-products/9231360506";
        Map<String,Object> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+token.getAccessToken());
        headers.put("Content-type","application/json");

        String httpResponse = httpUtils.get(url,headers);
        log.info("result = {}",httpResponse);
    }
}