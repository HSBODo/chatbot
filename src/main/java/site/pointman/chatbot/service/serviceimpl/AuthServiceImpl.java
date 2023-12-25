package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.property.Context;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.utill.HttpUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("naver.api.client.id")
    private String naverApiClientId;

    @Value("naver.api.client.secret_sign")
    private String naverApiClientSecretSign;

    @Value("secret.encrypt.key")
    private  String SECRET_ENCRYPT;

    private static String iv;
    private static Key keySpec;

    HttpUtils httpUtils;

    @Override
    public OAuthTokenDto createNaverOAuthToken() {
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

    @Override
    public String createJwtToken(String name, String userKey) {
        Date currentDate = new Date(System.currentTimeMillis());
        Long expiration = 1000* 60L * 60L * 1L; //유효시간
        Date expirationDate = new Date(currentDate.getTime()+ expiration);
        // Header
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // Payload
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("name", name);
        payloads.put("userKey", userKey);

        return Jwts.builder()
                .setSubject("token")
                .setHeader(headers)
                .setClaims(payloads)
                .signWith(SignatureAlgorithm.HS256,SECRET_ENCRYPT)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .compact();
    }


    @Override
    public boolean isTokenVerification(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_ENCRYPT).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature", e);
            return false;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
            return false;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
            return false;
        }
    }

    @Override
    public ChatBotResponse addJwtToken(ChatBotResponse chatBotResponse, String accessToken) {
        Context context = new Context("token",1,600);
        context.addParam("accessToken",accessToken);
        chatBotResponse.addContext(context);
        return chatBotResponse;
    }

    private Claims parseClaims(String accessToken) {
        return Jwts.parser()
                .setSigningKey(SECRET_ENCRYPT)
                .parseClaimsJws(accessToken)
                .getBody();
    }
    private boolean isExpired(String accessToken) {
        return Jwts.parser().
                setSigningKey(SECRET_ENCRYPT)
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    private String createSignature(Long timestamp) {
        // 밑줄로 연결하여 password 생성
        String password = naverApiClientId+"_"+timestamp;
        // bcrypt 해싱
        String hashedPw = BCrypt.hashpw(password, naverApiClientSecretSign);
        // base64 인코딩
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }


}
