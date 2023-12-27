package site.pointman.chatbot.service.chatbot.serviceImpl;

import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;
import site.pointman.chatbot.domain.response.property.components.CommerceCard;
import site.pointman.chatbot.domain.response.property.components.TextCard;
import site.pointman.chatbot.service.OrderService;
import site.pointman.chatbot.service.chatbot.OrderChatBotResponseService;
import site.pointman.chatbot.utill.CustomStringUtils;

import java.util.List;

@Service
public class OrderChatBotResponseServiceImpl implements OrderChatBotResponseService {

    OrderService orderService;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public OrderChatBotResponseServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ChatBotResponse purchaseReconfirm(String orderId) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        StringBuilder text = new StringBuilder();
        text
                .append("배송받은 상품을 꼼꼼히 확인하셨나요?")
                .append("\n")
                .append("구매확정 이후에는 취소가 어렵습니다.")
                .append("\n")
                .append("신중하게 고민하고 구매확정 버튼을 눌러주세요.")
        ;

        chatBotResponse.addSimpleText(text.toString());
        chatBotResponse.addQuickButton(ButtonName.구매확정.name(),ButtonAction.블럭이동,BlockId.PURCHASE_SUCCESS_CONFIRM.getBlockId(),ButtonParamKey.orderId,orderId);
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse purchaseConfirm(String orderId) {
        HttpResponse result = orderService.purchaseConfirm(orderId);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException(result.getMessage());

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("정상적으로 구매확정을 완료하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse salesReconfirm(String orderId) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        StringBuilder text = new StringBuilder();
        text
                .append("상품을 판매확정 하겠습니까?")
        ;

        chatBotResponse.addSimpleText(text.toString());
        chatBotResponse.addQuickButton(ButtonName.판매확정.name(),ButtonAction.블럭이동,BlockId.SALE_SUCCESS_CONFIRM.getBlockId(),ButtonParamKey.orderId,orderId);
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse salesConfirm(String orderId) {
        HttpResponse result = orderService.salesConfirm(orderId);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException(result.getMessage());

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("정상적으로 판매확정을 완료하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse updateTrackingNumber(String orderId, String trackingNumber) {
        HttpResponse result = orderService.updateTrackingNumber(orderId, trackingNumber);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException(result.getMessage());

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("운송장번호를 정상적으로 등록하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    private Carousel<BasicCard> createCarouselImage(List<String> imageUrls){
        Carousel<BasicCard> basicCardCarousel = new Carousel<>();
        imageUrls.forEach(imageUrl -> {
            BasicCard basicCard = new BasicCard();
            basicCard.setThumbnail(imageUrl,true);
            basicCardCarousel.addComponent(basicCard);
        });
        return basicCardCarousel;
    }
}
