package site.pointman.chatbot.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;


@Getter @Setter @ToString
public class KakaoUserRequest {
    private Map<String,Object> intent;
    private Map<String,Object> userRequest;
    private Map<String,Object> bot;
    private Map<String,Object> action;

    private String kakaoUserkey;
    private String uttr;

}
