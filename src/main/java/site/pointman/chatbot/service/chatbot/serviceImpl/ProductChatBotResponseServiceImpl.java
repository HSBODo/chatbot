package site.pointman.chatbot.service.chatbot.serviceImpl;

import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.property.Context;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.common.Link;
import site.pointman.chatbot.domain.response.property.common.Profile;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;
import site.pointman.chatbot.domain.response.property.components.CommerceCard;
import site.pointman.chatbot.domain.response.property.components.TextCard;
import site.pointman.chatbot.dto.product.SpecialProduct;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.service.chatbot.ProductChatBotResponseService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductChatBotResponseServiceImpl implements ProductChatBotResponseService {

    @Value("${host.url}")
    private String HOST_URL;

    MemberRepository memberRepository;
    OrderRepository orderRepository;

    public ProductChatBotResponseServiceImpl(MemberRepository memberRepository, OrderRepository orderRepository) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public ChatBotResponse updateStatusSuccessChatBotResponse(ProductStatus productStatus) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        if (productStatus.equals(ProductStatus.삭제)) {
            chatBotResponse.addSimpleText("상품을 삭제하였습니다.");
        }else {
            chatBotResponse.addSimpleText("상품을 "+productStatus+" 상태로 변경하였습니다.");
        }

        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse deleteProductSuccessChatBotResponse() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 정상적으로 삭제하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse verificationCustomerSuccessChatBotResponse() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 등록하시겠습니까?");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.등록하기.name(), ButtonAction.블럭이동, BlockId.PRODUCT_ADD_INFO.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getProductProfileSuccessChatBotResponse(String buyerUserKey, Product product) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        TextCard textCard = new TextCard();

        String productUserKey = product.getMember().getUserKey();

        Carousel<BasicCard> carouselImage = createCarouselImage(product.getProductImages().getImageUrls());

        chatBotResponse.addCarousel(carouselImage);
        textCard.setTitle(product.getName());
        textCard.setDescription(product.getProductProfileTypeOfChatBot());

        if (!product.getMember().getUserKey().equals(buyerUserKey) && product.getStatus().equals(ProductStatus.예약)) return new ChatBotExceptionResponse().createException("예약중인 상품입니다.");

        Optional<Member> buyerMember = memberRepository.findByUserKey(buyerUserKey);
        if(!buyerMember.isEmpty() && !productUserKey.equals(buyerUserKey) && product.getStatus().equals(ProductStatus.판매중)){


            /**
             * 카카오페이 결제버튼 노출조건
             * 1. 회원이어야 한다.
             * 2. 나의 상품은 내가 카카오페이 결제 할 수 없다.
             * 3. 판매중인 상품만 카카오페이 결제가 가능하다.
             */
            String kakaoPaymentUrl = product.getKakaoPaymentUrl(buyerUserKey,HOST_URL);

            Button button = new Button("카카오페이 결제(테스트)",ButtonAction.웹링크연결,kakaoPaymentUrl);
            textCard.setButtons(button);
        }

        chatBotResponse.addTextCard(textCard);

        chatBotResponse = createStatusQuickButtons(buyerUserKey, product.getMember().getUserKey(), chatBotResponse, product.getStatus(), String.valueOf(product.getId()));
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse createProductListChatBotResponse(List<Product> products, ButtonName quickButtonName, BlockId nextBlockId) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> carousel = new Carousel<>();

        products.forEach(product -> {
            CommerceCard commerceCard = new CommerceCard();

            String productName = product.getName();
            int productPrice = product.getPrice().intValue();

            String productDescription = "등록일자: " + product.getFormatCreateDate();
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            String productId = String.valueOf(product.getId());

            Button button = new Button("상세보기", ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(), ButtonParamKey.productId, productId);

            commerceCard.setThumbnails(thumbnailImageUrl,true);
            commerceCard.setProfile(product.getMember().getProfile());
            commerceCard.setTitle(productName);
            commerceCard.setDescription(productDescription);
            commerceCard.setPrice(productPrice);
            commerceCard.setButton(button);

            carousel.addComponent(commerceCard);
        });

        chatBotResponse.addCarousel(carousel);
        chatBotResponse.addQuickButton(quickButtonName.name(), ButtonAction.블럭이동, nextBlockId.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse createMyProductListChatBotResponse(List<Product> products, ButtonName quickButtonName, BlockId nextBlockId) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> carousel = new Carousel<>();

        products.forEach(product -> {
            CommerceCard commerceCard = new CommerceCard();
            StringBuilder productDescription = new StringBuilder();

            ProductStatus productStatus = product.getStatus();
            String productId = String.valueOf(product.getId());
            String productName = product.getName() + "("+productStatus+")";
            int productPrice = product.getPrice().intValue();
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            String createDate = product.getFormatCreateDate();

            productDescription
                    .append("등록일자: "+ createDate)
            ;

            Button button = new Button("상세보기", ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(), ButtonParamKey.productId, productId);

            commerceCard.setThumbnails(thumbnailImageUrl,true);
            commerceCard.setProfile(product.getMember().getProfile());
            commerceCard.setPrice(productPrice);
            commerceCard.setTitle(productName);
            commerceCard.setDescription(productDescription.toString());
            commerceCard.setButton(button);
            carousel.addComponent(commerceCard);
        });

        chatBotResponse.addCarousel(carousel);
        chatBotResponse.addQuickButton(quickButtonName.name(), ButtonAction.블럭이동, nextBlockId.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getProductInfoPreviewSuccessChatBotResponse(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        StringBuilder productPreviewProfile = new StringBuilder();
        Context productContext = new Context("product",1,600);
        productContext.addParam("productCategory",category);

        productPreviewProfile
                .append("카테고리: " + category)
                .append("\n\n")
                .append("판매가격: " + productPrice +"원")
                .append("\n\n")
                .append("상품 설명: " + productDescription)
                .append("\n\n")
                .append("거래 희망 장소: " + tradingLocation)
                .append("\n\n")
                .append("카카오 오픈 채팅방: " + kakaoOpenChatUrl);

        Carousel<BasicCard> carouselImage = createCarouselImage(imageUrls);
        chatBotResponse.addCarousel(carouselImage);
        chatBotResponse.addTextCard(productName,productPreviewProfile.toString());
        chatBotResponse.addQuickButton(ButtonName.취소.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.등록.name(), ButtonAction.블럭이동, BlockId.PRODUCT_ADD.getBlockId());
        chatBotResponse.addContext(productContext);
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse addProductSuccessChatBotResponse() {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 정상적으로 등록하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getCategoryChatBotResponse(BlockId nextBlockId) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        List<Category> categories = Arrays.stream(Category.values()).collect(Collectors.toList());

        chatBotResponse.addSimpleText("카테고리를 선택해주세요.");

        categories.forEach(category -> {
            chatBotResponse.addQuickButton(category.getValue(), ButtonAction.블럭이동, nextBlockId.getBlockId(), ButtonParamKey.choice, category.getValue());
        });

        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getContractProductsSuccessChatBotResponse(List<Product> contractProducts) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> carousel = new Carousel<>();

        contractProducts.forEach(product -> {
            Order order = orderRepository.findByProductId(product.getId(), OrderStatus.주문체결).get();
            CommerceCard commerceCard = new CommerceCard();
            StringBuilder productDescription = new StringBuilder();

            ProductStatus productStatus = product.getStatus();
            String orderId = String.valueOf(order.getOrderId());
            String productName = product.getName() + "("+productStatus+")";
            int productPrice = product.getPrice().intValue();
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            productDescription
                    .append("구매자: "+ order.getBuyerMember().getName())
                    .append("\n")
                    .append("결제일자: "+ order.getFormatApproveDate())
            ;

            commerceCard.setThumbnails(thumbnailImageUrl,true);
            commerceCard.setProfile(order.getProduct().getMember().getProfile());
            commerceCard.setPrice(productPrice);
            commerceCard.setTitle(productName);
            commerceCard.setDescription(productDescription.toString());
            commerceCard.setButton(new Button("상세보기", ButtonAction.블럭이동, BlockId.PRODUCT_GET_CONTRACT_PROFILE.getBlockId(), ButtonParamKey.orderId, orderId));
            carousel.addComponent(commerceCard);
        });

        chatBotResponse.addCarousel(carousel);
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getContractProductProfileSuccessChatBotResponse(Order order) {
        Product product = order.getProduct();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        TextCard textCard = new TextCard();

        Carousel<BasicCard> carouselImage = createCarouselImage(product.getProductImages().getImageUrls());
        chatBotResponse.addCarousel(carouselImage);

        StringBuilder productDescription = new StringBuilder();
        productDescription
                .append("상품상태: " + product.getStatus().getValue())
                .append("\n")
                .append("카테고리: " + product.getCategory().getValue())
                .append("\n\n")
                .append("판매가격: " + product.getFormatPrice()+"원")
                .append("\n\n")
                .append("상품 설명: " + product.getDescription())
                .append("\n\n")
                .append("거래 희망 장소: " + product.getTradingLocation())
                .append("\n")
                .append("카카오 오픈 채팅방: " + product.getKakaoOpenChatUrl())
                .append("\n\n")
                .append("등록일자: " + product.getFormatCreateDate())
                .append("\n")
                .append("운송장번호: " + order.viewTackingNumber())
                .append("\n\n")
                .append("구매자: " + order.getBuyerMember().getName())
                .append("\n")
                .append("결제일자: " + order.getFormatApproveDate());

        textCard.setTitle(product.getName());
        textCard.setDescription(productDescription.toString());

        chatBotResponse.addTextCard(textCard);



        if (order.getBuyerConfirmStatus().equals(OrderMemberConfirmStatus.구매확정)) {
            chatBotResponse.addQuickButton(ButtonName.판매확정.name(),ButtonAction.블럭이동,BlockId.SALE_SUCCESS_RECONFIRM.getBlockId(),ButtonParamKey.orderId,String.valueOf(order.getOrderId()));
        }else {
            String buttonName = "운송장번호 등록";
            if (!StringUtils.isNullOrEmpty(order.getTrackingNumber())) buttonName = "운송장번호 변경";
            chatBotResponse.addQuickButton(buttonName, ButtonAction.블럭이동, BlockId.ORDER_ADD_TRACKING_NUMBER.getBlockId(), ButtonParamKey.orderId, String.valueOf(order.getOrderId()));
        }
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getSpecialProductsSuccessChatBotResponse(List<SpecialProduct> specialProducts, int nextFirstNumber, int nextPage) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> commerceCardCarousel = new Carousel<>();

        Button nextButton = new Button(ButtonName.더보기.name(),ButtonAction.블럭이동,BlockId.PRODUCT_HOT_DEAL.getBlockId());

        nextButton.setExtra(ButtonParamKey.pageNumber,String.valueOf(nextPage));
        nextButton.setExtra(ButtonParamKey.firstNumber,String.valueOf(nextFirstNumber));

        specialProducts.forEach(specialProduct -> {
            CommerceCard commerceCard = new CommerceCard();
            Link thumbnailLink = new Link();
            thumbnailLink.setMobile(specialProduct.getPurchaseUrl());

            StringBuilder description = new StringBuilder();
            description
                    .append(specialProduct.getCategory())
                    .append("\n")
                    .append("금액단위: "+specialProduct.getCurrency());
            commerceCard.setThumbnails(specialProduct.getProductThumbnailImageUrl(), thumbnailLink,true);
            commerceCard.setProfile(new Profile(specialProduct.getBrandNameAndStatus(),specialProduct.getBrandImageUrl()));
            commerceCard.setPrice(specialProduct.getFormatPrice());
            commerceCard.setTitle(specialProduct.getTitle());
            commerceCard.setDescription(description.toString());
            commerceCard.setButton(new Button("상세보기",ButtonAction.웹링크연결,specialProduct.getDetailInfoUrl()));
            commerceCard.setButton(new Button("구매하러가기",ButtonAction.웹링크연결,specialProduct.getPurchaseUrl()));

            commerceCardCarousel.addComponent(commerceCard);
        });

        chatBotResponse.addCarousel(commerceCardCarousel);
        chatBotResponse.addQuickButton(ButtonName.메인으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton(nextButton);
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getMainProductsChatBotResponse(List<Product> products) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> commerceCardCarousel = new Carousel<>();

        products.forEach(product -> {
            CommerceCard commerceCard = new CommerceCard();

            String productName = product.getName();
            if (product.getStatus().equals(ProductStatus.예약)) productName = productName+"(예약중)";

            int productPrice = product.getPrice().intValue();

            String productDescription = "등록일자: " + product.getFormatCreateDate();
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            String productId = String.valueOf(product.getId());

            Button button = new Button("상세보기", ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(), ButtonParamKey.productId, productId);

            commerceCard.setThumbnails(thumbnailImageUrl,true);
            commerceCard.setProfile(product.getMember().getProfile());
            commerceCard.setTitle(productName);
            commerceCard.setDescription(productDescription);
            commerceCard.setPrice(productPrice);
            commerceCard.setButton(button);

            commerceCardCarousel.addComponent(commerceCard);
        });

        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        chatBotResponse.addCarousel(commerceCardCarousel);
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

    private ChatBotResponse createStatusQuickButtons(String buyerUserKey, String productUserKey, ChatBotResponse chatBotResponse, ProductStatus status, String productId){

        if(productUserKey.equals(buyerUserKey)) switch (status){
            //MyPage 상품 조회시
            case 판매중:
                chatBotResponse.addQuickButton(ButtonName.숨김.name(), ButtonAction.블럭이동, BlockId.PRODUCT_UPDATE_STATUS.getBlockId(), ButtonParamKey.productId, productId);
                chatBotResponse.addQuickButton(ButtonName.예약.name(), ButtonAction.블럭이동, BlockId.PRODUCT_UPDATE_STATUS.getBlockId(), ButtonParamKey.productId,productId);
                chatBotResponse.addQuickButton(ButtonName.삭제.name(), ButtonAction.블럭이동,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(), ButtonParamKey.productId, productId);
                chatBotResponse.addQuickButton(ButtonName.판매완료.name() ,ButtonAction.블럭이동, BlockId.PRODUCT_UPDATE_STATUS.getBlockId(), ButtonParamKey.productId, productId);
                chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
                return chatBotResponse;
            case 숨김:
                chatBotResponse.addQuickButton(ButtonName.판매중.name(), ButtonAction.블럭이동, BlockId.PRODUCT_UPDATE_STATUS.getBlockId(), ButtonParamKey.productId, productId);
                chatBotResponse.addQuickButton(ButtonName.삭제.name(), ButtonAction.블럭이동, BlockId.PRODUCT_UPDATE_STATUS.getBlockId());
                chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
                return chatBotResponse;
            case 예약:
                chatBotResponse.addQuickButton(ButtonName.예약취소.name() , ButtonAction.블럭이동, BlockId.PRODUCT_UPDATE_STATUS.getBlockId(), ButtonParamKey.productId, productId);
                chatBotResponse.addQuickButton(ButtonName.판매완료.name(), ButtonAction.블럭이동, BlockId.PRODUCT_UPDATE_STATUS.getBlockId(), ButtonParamKey.productId, productId);
                chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
                return chatBotResponse;
            case 판매완료:
                chatBotResponse.addQuickButton(ButtonName.삭제.name(), ButtonAction.블럭이동, BlockId.PRODUCT_UPDATE_STATUS.getBlockId(), ButtonParamKey.productId, productId);
                chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
                return chatBotResponse;
            default:
                chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
                return chatBotResponse;
        }

        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }
}
