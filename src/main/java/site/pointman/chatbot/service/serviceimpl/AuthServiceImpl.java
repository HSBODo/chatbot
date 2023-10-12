package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.utill.HttpUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${naver.api.client.id}")
    private String naverApiClientId;

    @Value("${naver.api.client.secret_sign}")
    private String naverApiClientSecretSign;

    HttpUtils httpUtils;

    @Override
    public String createSignature(Long timestamp) {
        // 밑줄로 연결하여 password 생성
        String password = naverApiClientId+"_"+timestamp;
        // bcrypt 해싱
        String hashedPw = BCrypt.hashpw(password, naverApiClientSecretSign);
        // base64 인코딩
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public OAuthTokenDto createToken() {
        Long timestamp = System.currentTimeMillis();
        timestamp = timestamp - 360L;
        log.info("timestamp= {} ",timestamp );


        String signature = createSignature(timestamp);

        String client_id = naverApiClientId;
        String client_secret_sign = signature;
        String grant_type = "client_credentials";
        String type = "SELF";
        String account_id = "";

        String url = "https://api.commerce.naver.com/external/v1/oauth2/token?client_id="+client_id+"&"+"timestamp="+timestamp+"&"+"client_secret_sign="+client_secret_sign+"&"+"grant_type="+grant_type+"&"+"type="+type+"&account_id="+account_id;
        MediaType mediaType = MediaType.parse("application/json");
        Map<String,Object> headers = new HashMap<>();
        headers.put("Content-type","application/json");
        Map<String,Object> body = new HashMap<>();
        String httpResponse = httpUtils.post(url, headers, body, mediaType);
        log.info("response = {}", httpResponse);
        try {
            ObjectMapper mapper = new ObjectMapper();
            OAuthTokenDto oAuthTokenDto = mapper.readValue(httpResponse, OAuthTokenDto.class);
            return oAuthTokenDto;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
