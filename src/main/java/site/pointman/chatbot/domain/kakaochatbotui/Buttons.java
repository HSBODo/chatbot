package site.pointman.chatbot.domain.kakaochatbotui;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter  @Setter
public class Buttons {
    private List buttons ;

    public Buttons() {
        this.buttons = new ArrayList<>();
    }

    public void addButton(Button button) {
        Map btn = new HashMap<>();
        btn.put("action",button.getAction());
        btn.put("label",button.getLabel());
        btn.put("webLinkUrl",button.getWebLinkUrl());
        this.buttons.add(btn);
    }

    public List createButtons(){
        return this.buttons;
    }
}
