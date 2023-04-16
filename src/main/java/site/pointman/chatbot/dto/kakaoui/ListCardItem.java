package site.pointman.chatbot.dto.kakaoui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.Map;

@Getter @Setter @AllArgsConstructor
public class ListCardItem {
    private String title;
    private String description;
    private String imageUrl;
    private Map<String,String> link;
}

