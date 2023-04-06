package site.pointman.chatbot.domain.kakaochatbotuiresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class Button {
    private String action;
    private String label;
    private String webLinkUrl;


}
