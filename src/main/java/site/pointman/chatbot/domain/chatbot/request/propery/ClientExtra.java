package site.pointman.chatbot.domain.chatbot.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientExtra {
    private String orderId;
    private String productId;
    private String noticeId;
    private String choice;
    private String productStatus;
    private String searchWord;
    private String pageNumber;
    private String firstNumber;
    private String lastNumber;
}
