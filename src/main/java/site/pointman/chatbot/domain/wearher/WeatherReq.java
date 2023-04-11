package site.pointman.chatbot.domain.wearher;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
public class WeatherReq {
    private String serviceKey;
    private String numOfRows;
    private String pageNo;
    private String dataType;
    private String base_date;
    private String base_time;
    private String nx;
    private String ny;
}
