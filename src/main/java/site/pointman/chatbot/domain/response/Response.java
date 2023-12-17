package site.pointman.chatbot.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Response {
    private Map<Object,Object> response = new HashMap<>();
}
