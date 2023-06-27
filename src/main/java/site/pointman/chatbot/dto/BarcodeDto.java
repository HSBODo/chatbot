package site.pointman.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.Map;


@Getter
@Setter
public class BarcodeDto {

    private String origin;

    @SuppressWarnings("unchecked")
    @JsonProperty("value")
    private void unpackNested(Map<String,Object> value) {
        this.origin = (String)value.get("origin");
    }

}
