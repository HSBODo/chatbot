package site.pointman.chatbot.domain.response.property.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Extra {
    private Map<String,String> extra;

    public Extra() {
        extra = new HashMap<>();
    }

    public Extra(Map<String, String> extra) {
        this.extra = extra;
    }

    public void addChoiceParam(String value){
        this.extra.put("choice",value);
    }
    public void addProductId(String value){
        this.extra.put("productId",value);
    }

    public String getProductId(){
        return extra.get("productId");
    }

    public String getChoiceParam(){
        return extra.get("choice");
    }
}
