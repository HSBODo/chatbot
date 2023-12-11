package site.pointman.chatbot.service;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;

@Slf4j
@SpringBootTest
class AuthServiceTest {
    @Autowired
    AuthService authService;

    @Test
    void createSignature() {
        Long timestamp = System.currentTimeMillis();
        String signature = authService.createSignature(timestamp);
        log.info("전자서명키 생성 = {}",signature);
    }

    @Test
    void createToken() {
        OAuthTokenDto token = authService.createToken();
        log.info("토큰 = {}",token.getAccessToken());
        Assertions.assertThat(token).isInstanceOf(OAuthTokenDto.class);
    }

    @Test
    void createJwtToken() {
        ChatBotRequest chatBotRequest = new ChatBotRequest();
        String jwtToken = authService.createJwtToken(chatBotRequest);
        log.info("jwtToken={}",jwtToken);

        Claims parseToekn = authService.parseClaims(jwtToken);
    }
}