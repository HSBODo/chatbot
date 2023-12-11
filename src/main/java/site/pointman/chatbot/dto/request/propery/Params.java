package site.pointman.chatbot.dto.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Params {
    private String enterName;
    private String enterPhone;
    private String enterAccount;
    private String productName;
    private String productDescription;
    private String productImg;
    private String productPrice;
}
