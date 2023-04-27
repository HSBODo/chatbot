package site.pointman.chatbot.vo.kakaoui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @AllArgsConstructor
public class ListCardItemVo {
    private String title;
    private String description;
    private String imageUrl;
    private Map<String,String> link;
}

