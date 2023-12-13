package site.pointman.chatbot.domain.response.property.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Extra {
    private Map<String,String> extra = new HashMap<>();

    public void addChoiceParam(String value){
        this.extra.put("choice",value);
    }
}
