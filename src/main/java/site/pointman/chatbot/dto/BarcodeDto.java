package site.pointman.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class BarcodeDto {

    private String origin;


    @JsonProperty("value")
    private void unpackNested(Map<String,Object> value) {
        this.origin = (String)value.get("origin");
    }

}
