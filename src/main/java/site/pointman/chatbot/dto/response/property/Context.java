package site.pointman.chatbot.dto.response.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import site.pointman.chatbot.dto.response.property.components.Carousel;

import java.util.HashMap;
import java.util.Map;
@Getter
public class Context {
    private String name;
    private int lifeSpan;
    private Map<String,String> params;

    public Context(String name, int lifeSpan) {
        this.name = name;
        this.lifeSpan = lifeSpan;
        this.params = new HashMap<>();
    }
    public void addParam(String key, String value ){
       this.params.put(key,value);
    }

}
