package site.pointman.chatbot.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class WeatherReqVo {
    private String serviceKey;
    private String numOfRows;
    private String pageNo;
    private String dataType;
    private String base_date;
    private String base_time;
    private String nx;
    private String ny;
}
