package site.pointman.chatbot.dto.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Properties {
    private String botUserKey;
    private boolean isFriend;
    private String plusfriendUserKey;
    private String bot_user_key;
    private String plusfriend_user_key;
}
