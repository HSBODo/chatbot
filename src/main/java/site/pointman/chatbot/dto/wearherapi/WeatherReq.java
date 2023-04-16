package site.pointman.chatbot.dto.wearherapi;

import lombok.Getter;
import lombok.Setter;


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
