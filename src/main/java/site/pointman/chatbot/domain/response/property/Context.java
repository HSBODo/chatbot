package site.pointman.chatbot.domain.response.property;


import lombok.Getter;


import java.util.HashMap;
import java.util.Map;

@Getter
public class Context {
    private String name;
    private int lifeSpan;
    private int ttl;
    private Map<String,Object> params;

    public Context(String name, int lifeSpan, int ttl) {
        this.name = name;
        this.lifeSpan = lifeSpan;
        this.ttl = ttl;
        this.params = new HashMap<>();
    }
    public void addParam(String key, Object value ){
       this.params.put(key,value);
    }

}
