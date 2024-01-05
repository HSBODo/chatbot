package site.pointman.chatbot.globalservice.serviceimpl;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.chatbot.response.property.Context;
import site.pointman.chatbot.globalservice.AuthService;

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
            log.error("Invalid JWT signature");
            return false;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
            return false;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
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
