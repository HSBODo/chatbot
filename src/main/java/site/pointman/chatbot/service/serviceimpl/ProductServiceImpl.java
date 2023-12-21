package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.response.property.Context;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;
import site.pointman.chatbot.domain.response.property.components.CommerceCard;
import site.pointman.chatbot.domain.response.property.components.TextCard;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.service.S3FileService;
import site.pointman.chatbot.utill.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    S3FileService s3FileService;

    ProductRepository productRepository;
    MemberRepository memberRepository;
    OrderRepository orderRepository;

    ChatBotExceptionResponse chatBotExceptionResponse;

    @Value("${host.url}")
    private String HOST_URL;

    public ProductServiceImpl(S3FileService s3FileService, ProductRepository productRepository, MemberRepository memberRepository, OrderRepository orderRepository) {
        this.s3FileService = s3FileService;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
    }

    @Override
    public Response addProduct(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, String productCategory) {
        try {
            Category category = Category.getCategory(productCategory);
            Member member = memberRepository.findByUserKey(userKey).get();
            String productName = productDto.getName();

            productDto.setStatus(ProductStatus.판매중);
            productDto.setCategory(category);
            productDto.setMember(member);
            productDto.setId(productId);

            ProductImageDto productImageDto = s3FileService.uploadProductImage(imageUrls, userKey,productName);

            productRepository.insertProduct(productDto,productImageDto);

            return addProductSuccessChatBotResponse();
        }catch (Exception e){
            return  chatBotExceptionResponse.createException();
        }
    }

    @Override
    public Response getProductCategory(String requestBlockId) {
        if(requestBlockId.equals(BlockId.PRODUCT_ADD_INFO.getBlockId()))  return getCategoryChatBotResponse(BlockId.PRODUCT_PROFILE_PREVIEW);

        return getCategoryChatBotResponse(BlockId.FIND_PRODUCTS_BY_CATEGORY);
    }

    @Override
    public Response getProductsByCategory(Category category) {
        try {
            List<Product> products = productRepository.findByCategory(category,ProductStatus.판매중);

            if(products.isEmpty()) return chatBotExceptionResponse.createException("등록된 상품이 없습니다.");

            return createProductListChatBotResponse(products,ButtonName.이전으로,BlockId.PRODUCT_GET_CATEGORIES);
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public Response getProductInfoPreview(List<String> imageUrls, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl, String category) {
        try {
            String formatPrice = StringUtils.formatPrice(Integer.parseInt(productPrice));

            return getProductInfoPreviewSuccessChatBotResponse(imageUrls, category, productName,productDescription,formatPrice,tradingLocation,kakaoOpenChatUrl);
        }catch (Exception e){
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public Response getMyProducts(String userKey, String productStatus) {
        try {
            ProductStatus status = ProductStatus.getProductStatus(productStatus);

            List<Product> products = productRepository.findByUserKey(userKey,status);

            if(products.isEmpty()) return chatBotExceptionResponse.createException("등록된 상품이 없습니다");

            return createMyProductListChatBotResponse(products,ButtonName.처음으로,BlockId.MAIN);
        }catch (Exception e){
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public Response getProductProfile(String productId, String userKey) {
        try {
            Optional<Product> mayBeProduct = productRepository.findByProductId(Long.parseLong(productId));

            if(mayBeProduct.isEmpty()) return chatBotExceptionResponse.createException("상품이 존재하지 않습니다.");
            
            Product product = mayBeProduct.get();

            return getProductProfileSuccessChatBotResponse(userKey, product);
        }catch (Exception e){
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public Response updateProductStatus(String productId, String utterance) {
        try {
            long parseProductId = Long.parseLong(productId);
            ProductStatus productStatus = ProductStatus.getProductStatus(utterance);

            Optional<Product> mayBeProduct = productRepository.findByProductId(parseProductId);
            if(mayBeProduct.isEmpty()) return chatBotExceptionResponse.createException("상품이 존재하지 않습니다.");

            productRepository.updateStatus(parseProductId,productStatus);

            return updateStatusSuccessChatBotResponse(productStatus);
        }catch (Exception e){
            return chatBotExceptionResponse.createException("상태변경을 실패하였습니다.");
        }
    }

    @Override
    public Response deleteProduct(String productId, String utterance) {
        try {
            long parseProductId = Long.parseLong(productId);

            Optional<Product> mayBeProduct = productRepository.findByProductId(parseProductId);
            if(mayBeProduct.isEmpty()) return chatBotExceptionResponse.createException("상품이 존재하지 않습니다.");

            if(ProductStatus.삭제.name().equals(utterance)){
                productRepository.deleteProduct(parseProductId);
                return deleteProductSuccessChatBotResponse();
            }

            return chatBotExceptionResponse.createException("상품 삭제를 실패하였습니다.");
        }catch (Exception e){
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public Response verificationCustomerSuccessResponse() {
       return verificationCustomerSuccessChatBotResponse();
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

    @Override
    public Response getProductsBySearchWord(String searchWord) {
        try {
            List<Product> products = productRepository.findBySearchWord(searchWord, ProductStatus.판매중);

            if(products.isEmpty()) return chatBotExceptionResponse.createException("등록된 상품이 없어 상품을 찾을수 없습니다.");

            return createProductListChatBotResponse(products,ButtonName.처음으로,BlockId.MAIN);
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public Response getContractProducts(String userKey) {
        List<Product> contractProducts = productRepository.findByUserKey(userKey, ProductStatus.판매대기);
        if(contractProducts.isEmpty()) return chatBotExceptionResponse.createException("결제가 체결된 상품이 없습니다.");

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<BasicCard> carousel = new Carousel<>();

        contractProducts.forEach(product -> {
            Order order = orderRepository.findByProductId(product.getId(), OrderStatus.주문체결).get();
            BasicCard basicCard = new BasicCard();
            StringBuilder productDescription = new StringBuilder();

            ProductStatus productStatus = product.getStatus();
            String orderId = String.valueOf(order.getOrderId());
            String productName = product.getName() + "("+productStatus+")";
            String productPrice = StringUtils.formatPrice(product.getPrice());
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);


            productDescription
                    .append("판매가격: "+ productPrice+"원")
                    .append("\n")
                    .append("결제일자: "+ order.getFormatApproveDate())
            ;

            Button button = new Button("상세보기", ButtonAction.블럭이동, BlockId.PRODUCT_GET_CONTRACT_PROFILE.getBlockId(), ButtonParamKey.orderId, orderId);

            basicCard.setThumbnail(thumbnailImageUrl,true);
            basicCard.setTitle(productName);
            basicCard.setDescription(productDescription.toString());
            basicCard.setButton(button);
            carousel.addComponent(basicCard);
        });

        chatBotResponse.addCarousel(carousel);
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public Response getContractProductProfile(String userKey, String orderId) {
        Optional<Order> mayBeOrder = orderRepository.findByOrderId(Long.parseLong(orderId),OrderStatus.주문체결);
        if (mayBeOrder.isEmpty()) return chatBotExceptionResponse.createException("체결된 주문이 없습니다.");

        Order order = mayBeOrder.get();
        Product product = order.getProduct();
        if(!userKey.equals(product.getMember().getUserKey())) return chatBotExceptionResponse.createException();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        TextCard textCard = new TextCard();

        Carousel<BasicCard> carouselImage = createCarouselImage(product.getProductImages().getImageUrls());
        chatBotResponse.addCarousel(carouselImage);

        String trackingNumber = order.getTrackingNumber();
        if (trackingNumber.isEmpty())trackingNumber = "미입력";

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
                .append("운송장번호: " + trackingNumber)
                .append("\n\n")
                .append("구매자: " + order.getBuyerMember().getUserKey())
                .append("\n")
                .append("결제일자: " + order.getFormatApproveDate());

        textCard.setTitle(product.getName());
        textCard.setDescription(productDescription.toString());

        chatBotResponse.addTextCard(textCard);

        if (order.getTrackingNumber().isEmpty()) {
            chatBotResponse.addQuickButton(ButtonName.운송장번호등록.name(), ButtonAction.블럭이동, BlockId.ORDER_ADD_TRACKING_NUMBER.getBlockId(), ButtonParamKey.orderId, String.valueOf(order.getOrderId()));
        }else {
            chatBotResponse.addQuickButton(ButtonName.운송장번호변경.name(), ButtonAction.블럭이동, BlockId.ORDER_ADD_TRACKING_NUMBER.getBlockId(), ButtonParamKey.orderId, String.valueOf(order.getOrderId()));
        }
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        return chatBotResponse;
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

    private ChatBotResponse updateStatusSuccessChatBotResponse(ProductStatus productStatus){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 "+productStatus+" 상태로 변경하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse deleteProductSuccessChatBotResponse(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 정상적으로 삭제하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse verificationCustomerSuccessChatBotResponse(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 등록하시겠습니까?");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.등록하기.name(), ButtonAction.블럭이동, BlockId.PRODUCT_ADD_INFO.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse getProductProfileSuccessChatBotResponse(String buyerUserKey, Product product){
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        TextCard textCard = new TextCard();

        String productUserKey = product.getMember().getUserKey();

        Carousel<BasicCard> carouselImage = createCarouselImage(product.getProductImages().getImageUrls());

        chatBotResponse.addCarousel(carouselImage);
        textCard.setTitle(product.getName());
        textCard.setDescription(product.getProductProfileTypeOfChatBot());

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

    private ChatBotResponse createProductListChatBotResponse(List<Product> products, ButtonName quickButtonName, BlockId nextBlockId){
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> carousel = new Carousel<>();

        products.forEach(product -> {
            CommerceCard commerceCard = new CommerceCard();

            String productName = product.getName();
            int productPrice = product.getPrice().intValue();

            String productDescription = "판매자: " + product.getMember().getName();
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            String productId = String.valueOf(product.getId());

            Button button = new Button("상세보기", ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(), ButtonParamKey.productId, productId);

            commerceCard.setThumbnails(thumbnailImageUrl,true);
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

    private ChatBotResponse createMyProductListChatBotResponse(List<Product> products, ButtonName quickButtonName, BlockId nextBlockId){
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<BasicCard> carousel = new Carousel<>();

        products.forEach(product -> {
            BasicCard basicCard = new BasicCard();
            StringBuilder productDescription = new StringBuilder();

            ProductStatus productStatus = product.getStatus();
            String productId = String.valueOf(product.getId());
            String productName = product.getName() + "("+productStatus+")";
            String productPrice = StringUtils.formatPrice(product.getPrice());
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            String createDate = product.getFormatCreateDate();

            productDescription
                    .append("판매가격: "+ productPrice+"원")
                    .append("\n")
                    .append("등록일자: "+ createDate)
            ;

            Button button = new Button("상세보기", ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(), ButtonParamKey.productId, productId);

            basicCard.setThumbnail(thumbnailImageUrl,true);
            basicCard.setTitle(productName);
            basicCard.setDescription(productDescription.toString());
            basicCard.setButton(button);
            carousel.addComponent(basicCard);
        });

        chatBotResponse.addCarousel(carousel);
        chatBotResponse.addQuickButton(quickButtonName.name(), ButtonAction.블럭이동, nextBlockId.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse getProductInfoPreviewSuccessChatBotResponse(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl){
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

    private ChatBotResponse addProductSuccessChatBotResponse(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 정상적으로 등록하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse getCategoryChatBotResponse(BlockId nextBlockId){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        List<Category> categories = Arrays.stream(Category.values()).collect(Collectors.toList());

        chatBotResponse.addSimpleText("카테고리를 선택해주세요.");

        categories.forEach(category -> {
            chatBotResponse.addQuickButton(category.getValue(), ButtonAction.블럭이동, nextBlockId.getBlockId(), ButtonParamKey.choice, category.getValue());
        });

        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }
}
