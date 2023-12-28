package site.pointman.chatbot.service.chatbot.serviceImpl;

import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.domain.response.property.Context;
import site.pointman.chatbot.domain.response.property.common.Button;
import site.pointman.chatbot.domain.response.property.common.Link;
import site.pointman.chatbot.domain.response.property.common.Profile;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;
import site.pointman.chatbot.domain.response.property.components.CommerceCard;
import site.pointman.chatbot.domain.response.property.components.TextCard;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.SpecialProduct;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.service.CrawlingService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.service.RedisService;
import site.pointman.chatbot.service.chatbot.ProductChatBotResponseService;
import site.pointman.chatbot.utill.CustomStringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductChatBotResponseServiceImpl implements ProductChatBotResponseService {

    @Value("${host.url}")
    private String HOST_URL;
    private ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    MemberRepository memberRepository;
    OrderRepository orderRepository;

    ProductService productService;
    CrawlingService crawlingService;
    RedisService redisService;



    public ProductChatBotResponseServiceImpl(MemberRepository memberRepository, OrderRepository orderRepository, ProductService productService, CrawlingService crawlingService, RedisService redisService) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.crawlingService = crawlingService;
        this.redisService = redisService;
    }

    @Override
    public ChatBotResponse updateStatusChatBotResponse(String productId, String utterance) {
        ProductStatus productStatus = ProductStatus.getProductStatus(utterance);
        if (productStatus.equals(ProductStatus.예약취소)) productStatus = ProductStatus.판매중;

        HttpResponse result = productService.updateProductStatus(Long.parseLong(productId), productStatus);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException("상품의 상태변경을 실패하였습니다.");

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        if (utterance.equals(ProductStatus.삭제.name())) {
            chatBotResponse.addSimpleText("상품을 삭제하였습니다.");
        }else {
            chatBotResponse.addSimpleText("상품을 "+productStatus.getValue()+" 상태로 변경하였습니다.");
        }

        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse deleteProductChatBotResponse(String productId, String utterance) {
        if(!ProductStatus.삭제.name().equals(utterance)) return chatBotExceptionResponse.createException("상품 삭제를 실패하였습니다.");

        HttpResponse result = productService.deleteProduct(Long.parseLong(productId));
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException(result.getMessage());
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
    public ChatBotResponse getProductChatBotResponse(String userKey, String productId) {
        HttpResponse result = productService.getProduct(Long.parseLong(productId));
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException("상품 조회를 실패하였습니다.");
        Product product = (Product) result.getResult();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        TextCard textCard = new TextCard();

        String productUserKey = product.getMember().getUserKey();

        Carousel<BasicCard> carouselImage = createCarouselImage(product.getProductImages().getImageUrls());

        chatBotResponse.addCarousel(carouselImage);
        textCard.setTitle(product.getName());
        textCard.setDescription(product.getProductProfileTypeOfChatBot());

        if (!product.getMember().getUserKey().equals(userKey) && product.getStatus().equals(ProductStatus.예약)) return new ChatBotExceptionResponse().createException("예약중인 상품입니다.");

        Optional<Member> buyerMember = memberRepository.findByUserKey(userKey);
        if(!buyerMember.isEmpty() && !productUserKey.equals(userKey) && product.getStatus().equals(ProductStatus.판매중)){
            /**
             * 카카오페이 결제버튼 노출조건
             * 1. 회원이어야 한다.
             * 2. 나의 상품은 내가 카카오페이 결제 할 수 없다.
             * 3. 판매중인 상품만 카카오페이 결제가 가능하다.
             */
            String kakaoPaymentUrl = product.getKakaoPaymentUrl(userKey,HOST_URL);

            Button button = new Button("카카오페이 결제(테스트)",ButtonAction.웹링크연결,kakaoPaymentUrl);
            textCard.setButtons(button);
        }

        chatBotResponse.addTextCard(textCard);

        chatBotResponse = createStatusQuickButtons(userKey, product.getMember().getUserKey(), chatBotResponse, product.getStatus(), String.valueOf(product.getId()));
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getProductsByCategoryChatBotResponse(Category category, int pageNumber) {
        HttpResponse result = productService.getProductsByCategory(category, pageNumber);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException("등록된 상품이 없습니다.");

        Page<Product> products = (Page<Product>) result.getResult();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> carousel = new Carousel<>();

        products.getContent().forEach(product -> {
            CommerceCard commerceCard = new CommerceCard();

            String productName = product.getName();
            if(product.getStatus().equals(ProductStatus.예약)) productName = productName+"(예약중)";

            int productPrice = product.getPrice().intValue();
            StringBuilder productDescription = new StringBuilder();
            productDescription
                    .append(product.getCategory().getValue())
                    .append("\n")
                    .append("등록일자: " + product.getFormatCreateDate());
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            String productId = String.valueOf(product.getId());

            Button button = new Button("상세보기", ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(), ButtonParamKey.productId, productId);

            commerceCard.setThumbnails(thumbnailImageUrl,true);
            commerceCard.setProfile(product.getMember().getProfile());
            commerceCard.setTitle(productName);
            commerceCard.setDescription(productDescription.toString());
            commerceCard.setPrice(productPrice);
            commerceCard.setButton(button);

            carousel.addComponent(commerceCard);
        });

        chatBotResponse.addCarousel(carousel);
        chatBotResponse.addQuickButton(ButtonName.카테고리.name(), ButtonAction.블럭이동, BlockId.PRODUCT_GET_CATEGORIES.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        if (products.hasNext()){
            Button nextButton = new Button(ButtonName.더보기.name(), ButtonAction.블럭이동, BlockId.FIND_PRODUCTS_BY_CATEGORY.getBlockId());
            nextButton.setExtra(ButtonParamKey.choice,category.getValue());
            nextButton.setExtra(ButtonParamKey.pageNumber,String.valueOf(++pageNumber));
            chatBotResponse.addQuickButton(nextButton);
        }
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse searchProductsChatBotResponse(String searchWord, int pageNumber) {
        HttpResponse result = productService.getProductsBySearchWord(searchWord,pageNumber);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException(result.getMessage());

        Page<Product> products = (Page<Product>) result.getResult();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> carousel = new Carousel<>();

        products.getContent().forEach(product -> {
            CommerceCard commerceCard = new CommerceCard();

            String productName = product.getName();
            if (product.getStatus().equals(ProductStatus.예약)) productName = productName+"(예약중)";

            int productPrice = product.getPrice().intValue();
            StringBuilder productDescription = new StringBuilder();
            productDescription
                    .append(product.getCategory().getValue())
                    .append("\n")
                    .append("등록일자: " + product.getFormatCreateDate());
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            String productId = String.valueOf(product.getId());

            Button button = new Button("상세보기", ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(), ButtonParamKey.productId, productId);

            commerceCard.setThumbnails(thumbnailImageUrl,true);
            commerceCard.setProfile(product.getMember().getProfile());
            commerceCard.setTitle(productName);
            commerceCard.setDescription(productDescription.toString());
            commerceCard.setPrice(productPrice);
            commerceCard.setButton(button);

            carousel.addComponent(commerceCard);
        });

        chatBotResponse.addCarousel(carousel);
        chatBotResponse.addQuickButton(ButtonName.상품검색.name(), ButtonAction.블럭이동, BlockId.PRODUCT_SEARCH.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        if (products.hasNext()) {
            Button nextButton = new Button(ButtonName.더보기.name(), ButtonAction.블럭이동, BlockId.PRODUCT_SEARCH_NEXT.getBlockId());
            nextButton.setExtra(ButtonParamKey.pageNumber,String.valueOf(++pageNumber));
            nextButton.setExtra(ButtonParamKey.searchWord,searchWord);
            chatBotResponse.addQuickButton(nextButton);
        }
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getMyProductsByStatusChatBotResponse(String userKey, String findStatus, int pageNumber) {
        ProductStatus status = ProductStatus.getProductStatus(findStatus);

        HttpResponse result = productService.getMemberProductsByStatus(userKey, status, pageNumber);

        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException("등록된 상품이 없습니다.");

        Page<Product> products = (Page<Product>) result.getResult();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> carousel = new Carousel<>();

        products.getContent().forEach(product -> {
            CommerceCard commerceCard = new CommerceCard();
            StringBuilder productDescription = new StringBuilder();

            ProductStatus productStatus = product.getStatus();
            String productId = String.valueOf(product.getId());
            String productName = product.getName() + "("+productStatus+")";
            int productPrice = product.getPrice().intValue();
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            String createDate = product.getFormatCreateDate();

            productDescription
                    .append(product.getCategory().getValue())
                    .append("\n")
                    .append("등록일자: " + createDate);

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
        chatBotResponse.addQuickButton(ButtonName.이전으로.name(), ButtonAction.블럭이동, BlockId.SALES_HISTORY_PAGE.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        if (products.hasNext()) {
            Button nextButton = new Button(ButtonName.더보기.name(), ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCTS.getBlockId());
            nextButton.setExtra(ButtonParamKey.pageNumber,String.valueOf(++pageNumber));
            nextButton.setExtra(ButtonParamKey.productStatus,status.getValue());
            chatBotResponse.addQuickButton(nextButton);
        }
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getProductInfoPreviewChatBotResponse(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl) {

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        StringBuilder productPreviewProfile = new StringBuilder();
        Context productContext = new Context("product",1,600);
        productContext.addParam("productCategory",category);

        String formatPrice = CustomStringUtils.formatPrice(Integer.parseInt(productPrice));
        productPreviewProfile
                .append("카테고리: " + category)
                .append("\n\n")
                .append("판매가격: " + formatPrice +"원")
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
    public ChatBotResponse addProductChatBotResponse(ProductDto productDto, String userKey, List<String> imageUrls) {
        HttpResponse result = productService.addProduct(productDto, userKey, imageUrls);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException("상품등록을 실패하였습니다.");

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 정상적으로 등록하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getCategoryChatBotResponse(String blockId) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        BlockId nextBlockId = BlockId.FIND_PRODUCTS_BY_CATEGORY;
        if(blockId.equals(BlockId.PRODUCT_ADD_INFO.getBlockId())) nextBlockId = BlockId.PRODUCT_PROFILE_PREVIEW;

        List<Category> categories = Arrays.stream(Category.values()).collect(Collectors.toList());

        chatBotResponse.addSimpleText("카테고리를 선택해주세요.");

        for (Category category : categories){
            chatBotResponse.addQuickButton(category.getValue(), ButtonAction.블럭이동, nextBlockId.getBlockId(), ButtonParamKey.choice, category.getValue());
        }
        chatBotResponse.addQuickButton(ButtonName.처음으로.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getSalesContractProductsChatBotResponse(String userKey, int pageNumber) {
        HttpResponse result = productService.getSalesContractProducts(userKey,pageNumber);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException("판매대기 중인 상품이 존재하지 않습니다.");
        Page<Product> contractProducts = (Page<Product>) result.getResult();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> carousel = new Carousel<>();

        contractProducts.getContent().forEach(product -> {
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
        chatBotResponse.addQuickButton(ButtonName.이전으로.name(),ButtonAction.블럭이동,BlockId.SALES_HISTORY_PAGE.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        if (contractProducts.hasNext()) {
            chatBotResponse.addQuickButton(new Button(ButtonName.더보기.name(), ButtonAction.블럭이동, BlockId.PRODUCT_GET_CONTRACT.getBlockId(),ButtonParamKey.pageNumber,String.valueOf(++pageNumber)));
        }
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getSalesContractProductChatBotResponse(String userKey, String orderId) {
        HttpResponse result = productService.getSalesContractProduct(userKey, Long.parseLong(orderId));
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException("체결된 주문이 없습니다.");
        Order order = (Order) result.getResult();

        Product product = order.getProduct();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        TextCard textCard = new TextCard();

        Carousel<BasicCard> carouselImage = createCarouselImage(product.getProductImages().getImageUrls());
        chatBotResponse.addCarousel(carouselImage);

        StringBuilder productDescription = new StringBuilder();
        productDescription
                .append(order.getProduct().getProductProfileTypeOfChatBot())
                .append("\n\n")
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
    public ChatBotResponse getSpecialProductsChatBotResponse(int pageNumber, int firstNumber) {
        try {
            if (pageNumber == 0) pageNumber++;
            int lastProduct = firstNumber+5;
            String url = "https://quasarzone.com/bbs/qb_saleinfo?page="+pageNumber;
            String cssQuery = "#frmSearch > div > div.list-board-wrap > div.market-type-list.market-info-type-list.relative > table > tbody > tr";

            Elements jsoupElements = crawlingService.getJsoupElements(url, cssQuery);
            List<Element> filterElements = crawlingService.filterElements(jsoupElements);

            List<SpecialProduct> specialProducts;
            specialProducts = redisService.isSameSpecialProduct(pageNumber, firstNumber, lastProduct, filterElements);

            if (specialProducts.isEmpty()) {
                int cnt = firstNumber;

                specialProducts = crawlingService.getSpecialProducts(filterElements,firstNumber,lastProduct);

                for (SpecialProduct specialProduct : specialProducts) {
                    log.info("setRedisKey={}",pageNumber+"-"+cnt);
                    redisService.setRedisSpecialProductValue(pageNumber+"-"+cnt,specialProduct);
                    cnt++;
                }
            }

            int nextFirstProduct= lastProduct;
            int nextPage = pageNumber;

            if (filterElements.size() <= lastProduct){
                nextFirstProduct = 0;
                nextPage++;
            }
            log.info("nextNum={}",nextFirstProduct);
            log.info("nextPage={}",nextPage);
            ChatBotResponse chatBotResponse = new ChatBotResponse();
            Carousel<CommerceCard> commerceCardCarousel = new Carousel<>();

            Button nextButton = new Button(ButtonName.더보기.name(),ButtonAction.블럭이동,BlockId.PRODUCT_HOT_DEAL.getBlockId());

            nextButton.setExtra(ButtonParamKey.pageNumber,String.valueOf(nextPage));
            nextButton.setExtra(ButtonParamKey.firstNumber,String.valueOf(nextFirstProduct));

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
        }catch (Exception e) {
            log.info("e={}",e.getStackTrace());
            return chatBotExceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getMainProductsChatBotResponse(int currentPage) {
        HttpResponse result = productService.getMainProducts(currentPage);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException("상품조회를 실패하였습니다.");
        Page<Product> products = (Page<Product>) result.getResult();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> commerceCardCarousel = new Carousel<>();

        products.getContent().forEach(product -> {
            CommerceCard commerceCard = new CommerceCard();

            String productName = product.getName();
            if (product.getStatus().equals(ProductStatus.예약)) productName = productName+"(예약중)";

            int productPrice = product.getPrice().intValue();

            StringBuilder productDescription = new StringBuilder();
            productDescription
                    .append(product.getCategory().getValue())
                    .append("\n")
                    .append("등록일자: " + product.getFormatCreateDate());
            String thumbnailImageUrl = product.getProductImages().getImageUrls().get(0);
            String productId = String.valueOf(product.getId());

            Button button = new Button("상세보기", ButtonAction.블럭이동, BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(), ButtonParamKey.productId, productId);

            commerceCard.setThumbnails(thumbnailImageUrl,true);
            commerceCard.setProfile(product.getMember().getProfile());
            commerceCard.setTitle(productName);
            commerceCard.setDescription(productDescription.toString());
            commerceCard.setPrice(productPrice);
            commerceCard.setButton(button);

            commerceCardCarousel.addComponent(commerceCard);
        });

        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        if (products.hasNext()) chatBotResponse.addQuickButton(ButtonName.더보기.name(),ButtonAction.블럭이동,BlockId.PRODUCT_GET_MAIN.getBlockId(),ButtonParamKey.pageNumber,String.valueOf(currentPage+1));
        chatBotResponse.addCarousel(commerceCardCarousel);
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getPurchaseProducts(String userKey, int pageNumber) {
        HttpResponse result = productService.getPurchaseProducts(userKey,pageNumber);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException("구매내역이 없습니다.");
        Page<Order> purchaseOrders = (Page<Order>) result.getResult();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<CommerceCard> basicCardCarousel = new Carousel<>();

        purchaseOrders.getContent().forEach(order -> {
            CommerceCard commerceCard = new CommerceCard();

            Product product = order.getProduct();

            String orderId = String.valueOf(order.getOrderId());
            String status = product.getStatus().getOppositeValue();

            String title = product.getName();
            String description = new StringBuilder()
                    .append("상품상태: "+status)
                    .append("\n")
                    .append("운송장번호: "+order.viewTackingNumber())
                    .toString();

            commerceCard.setThumbnails(product.getProductImages().getImageUrls().get(0),true);
            commerceCard.setProfile(order.getProduct().getMember().getProfile());
            commerceCard.setPrice(order.getProduct().getPrice().intValue());
            commerceCard.setTitle(title);
            commerceCard.setDescription(description);
            commerceCard.setButton(new Button("상세보기", ButtonAction.블럭이동, BlockId.PRODUCT_GET_PURCHASE_PROFILE.getBlockId(), ButtonParamKey.orderId, orderId));

            basicCardCarousel.addComponent(commerceCard);
        });

        chatBotResponse.addCarousel(basicCardCarousel);
        chatBotResponse.addQuickButton(new Button(ButtonName.이전으로.name(), ButtonAction.블럭이동, BlockId.MY_PAGE.getBlockId()));
        chatBotResponse.addQuickButton(new Button(ButtonName.메인메뉴.name(), ButtonAction.블럭이동, BlockId.MAIN.getBlockId()));
        if (purchaseOrders.hasNext()) {
            chatBotResponse.addQuickButton(new Button(ButtonName.더보기.name(), ButtonAction.블럭이동, BlockId.PRODUCT_GET_PURCHASE.getBlockId(),ButtonParamKey.pageNumber,String.valueOf(++pageNumber)));
        }
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getPurchaseProductProfile(String userKey, String orderId) {
        HttpResponse result = productService.getPurchaseProduct(userKey, orderId);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException(result.getMessage());
        Order order = (Order) result.getResult();

        Product product = order.getProduct();

        ChatBotResponse chatBotResponse = new ChatBotResponse();
        TextCard textCard = new TextCard();

        Carousel<BasicCard> carouselImage = createCarouselImage(product.getProductImages().getImageUrls());

        chatBotResponse.addCarousel(carouselImage);

        textCard.setTitle(product.getName());
        textCard.setDescription(order.getPurchaseProductProfile());

        chatBotResponse.addTextCard(textCard);

        if(!StringUtils.isNullOrEmpty(order.getTrackingNumber()) &&
                !order.getStatus().equals(OrderStatus.거래완료) &&
                !order.getBuyerConfirmStatus().equals(OrderMemberConfirmStatus.구매확정))
            chatBotResponse.addQuickButton(new Button(ButtonName.구매확정.name(), ButtonAction.블럭이동, BlockId.PURCHASE_SUCCESS_RECONFIRM.getBlockId(), ButtonParamKey.orderId,orderId));

        chatBotResponse.addQuickButton(new Button(ButtonName.처음으로.name(),ButtonAction.블럭이동,BlockId.MAIN.getBlockId()));
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
