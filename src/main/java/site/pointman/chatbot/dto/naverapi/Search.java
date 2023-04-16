package site.pointman.chatbot.dto.naverapi;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
@Getter @Setter
public class Search {

    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List items = new ArrayList<>();

    public void setItems(JSONArray items) {
        items.forEach(item -> {
            this.items.add(item);
        });
    }
}
