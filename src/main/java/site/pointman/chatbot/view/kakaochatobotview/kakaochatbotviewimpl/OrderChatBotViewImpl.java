package site.pointman.chatbot.view.kakaochatobotview.kakaochatbotviewimpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.chatbot.constatnt.block.BlockId;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonAction;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonName;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonParamKey;
import site.pointman.chatbot.domain.chatbot.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.log.response.Response;
import site.pointman.chatbot.domain.chatbot.response.property.components.BasicCard;
import site.pointman.chatbot.domain.chatbot.response.property.components.Carousel;
import site.pointman.chatbot.domain.order.service.OrderService;
import site.pointman.chatbot.domain.log.response.constant.ResultCode;
import site.pointman.chatbot.view.kakaochatobotview.OrderChatBotView;

import java.util.List;

@Service
public class OrderChatBotViewImpl implements OrderChatBotView {

    OrderService orderService;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public OrderChatBotViewImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ChatBotResponse purchaseReconfirmPage(String orderId) {
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
        chatBotResponse.addQuickButton(ButtonName.구매확정.name(), ButtonAction.블럭이동, BlockId.PURCHASE_SUCCESS_CONFIRM.getBlockId(), ButtonParamKey.orderId,orderId);
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse purchaseConfirmResultPage() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("정상적으로 구매확정을 완료하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse salesReconfirmPage(String orderId) {
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
    public ChatBotResponse salesConfirmResultPage() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("정상적으로 판매확정을 완료하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse updateTrackingNumberResultPage() {
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
