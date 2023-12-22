package site.pointman.chatbot.service.chatbot.serviceImpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;
import site.pointman.chatbot.domain.response.property.components.TextCard;
import site.pointman.chatbot.service.chatbot.OrderChatBotResponseService;
import site.pointman.chatbot.utill.StringUtils;

import java.util.List;

@Service
public class OrderChatBotResponseServiceImpl implements OrderChatBotResponseService {

    @Override
    public ChatBotResponse getPurchaseProducts(List<Order> purchaseOrders) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Button button = new Button(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        Carousel<BasicCard> basicCardCarousel = new Carousel<>();

        purchaseOrders.forEach(order -> {
            BasicCard basicCard = new BasicCard();

            Product product = order.getProduct();

            String orderId = String.valueOf(order.getOrderId());
            String status = product.getStatus().getOppositeValue();

            String title = product.getName();
            String description = new StringBuilder()
                    .append("판매가격: "+ StringUtils.formatPrice(product.getPrice())+"원")
                    .append("\n")
                    .append("상품상태: "+status)
                    .toString();

            Button detailButton =new Button("상세보기", ButtonAction.블럭이동, BlockId.PRODUCT_GET_PURCHASE_PROFILE.getBlockId(), ButtonParamKey.orderId, orderId);
            basicCard.setThumbnail(product.getProductImages().getImageUrls().get(0),true);
            basicCard.setTitle(title);
            basicCard.setDescription(description);
            basicCard.setButton(detailButton);

            basicCardCarousel.addComponent(basicCard);
        });

        chatBotResponse.addCarousel(basicCardCarousel);
        chatBotResponse.addQuickButton(button);
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getPurchaseProductProfile(Order order) {
        String orderId = String.valueOf(order.getOrderId());
        Product product = order.getProduct();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        TextCard textCard = new TextCard();

        Carousel<BasicCard> carouselImage = createCarouselImage(product.getProductImages().getImageUrls());

        chatBotResponse.addCarousel(carouselImage);

        textCard.setTitle(product.getName());
        textCard.setDescription(order.getPurchaseProductProfile());

        chatBotResponse.addTextCard(textCard);

        if(!order.getTrackingNumber().isEmpty() && !order.getStatus().equals(OrderStatus.거래완료)) chatBotResponse.addQuickButton(new Button(ButtonName.구매확정.name(), ButtonAction.블럭이동, BlockId.PURCHASE_SUCCESS_RECONFIRM.getBlockId(), ButtonParamKey.orderId,orderId));
        chatBotResponse.addQuickButton(new Button(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId()));
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse purchaseSuccessReconfirm(String orderId) {
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
    public ChatBotResponse purchaseSuccessConfirm(Order order) {
        order.orderSuccessConfirm();

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("정상적으로 구매확정을 완료하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse updateTrackingNumber() {
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
