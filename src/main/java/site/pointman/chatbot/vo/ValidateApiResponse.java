package site.pointman.chatbot.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ValidateApiResponse {

    private ValidationApiStatusType status;
    private String value;
    private Map<String,String> data;

    public ValidateApiResponse(ValidationApiStatusType status, String value, Map<String, String> data) {
        this.status = status;
        this.value = value;
        this.data = data;
    }

    public String createJsonResponse() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(this);
        return jsonInString;
    }
}
