package site.pointman.chatbot.domain.chatbot.request.propery;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Params {
    private String customerName;
    private String customerPhone;
    private String productName;
    private String productDescription;
    private String productImg;
    private String productPrice;
    private String kakaoOpenChatUrl;
    private String tradingLocation;
    private String searchWord;
    private String reservationCustomerName;
    private String trackingNumber;
    private String customerProfileImage;
}
