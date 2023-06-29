package site.pointman.chatbot.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ContextsVo {
    private String name;
    private int lifespan;
    private int ttl;
    private Map<String,Object> params;

}
