package site.pointman.chatbot.dto.notice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;


@Getter
public class NoticeListDto {

    @JsonProperty("contents")
    private List<NoticeDto> noticeListDto;
}
