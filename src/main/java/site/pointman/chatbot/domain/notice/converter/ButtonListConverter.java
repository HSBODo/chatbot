package site.pointman.chatbot.domain.notice.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import site.pointman.chatbot.domain.chatbot.response.property.common.Button;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.util.List;

@Convert
public class ButtonListConverter implements AttributeConverter<List<Button>,String> {
    private final ObjectMapper mapper = new ObjectMapper();


    /*
     * 데이터베이스에 요청을 보낼 때
     * */
    @Override
    public String convertToDatabaseColumn(List<Button> dataList) {
        try {
            return mapper.writeValueAsString(dataList);
        }catch (JsonProcessingException e){
           throw new RuntimeException(e);
        }
    }

    /*
     * 데이터베이스에서 값을 읽어올 때
     * */

    @Override
    public List<Button> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data, new TypeReference<List<Button>>() {});
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
