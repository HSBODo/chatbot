package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.BlockId;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.customer.Customer;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.response.property.Context;
import site.pointman.chatbot.domain.response.property.common.Extra;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.CustomerRepository;
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
    CustomerRepository customerRepository;

    ChatBotExceptionResponse chatBotExceptionResponse;

    public ProductServiceImpl(S3FileService s3FileService, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.s3FileService = s3FileService;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.chatBotExceptionResponse = new ChatBotExceptionResponse();
    }

    @Override
    public Response addProduct(ProductDto productDto, Long productId, String userKey, List<String> imageUrls, String productCategory) {
        try {
            Category category = Category.getCategory(productCategory);
            Customer customer = customerRepository.findByCustomer(userKey).get();
            String productName = productDto.getName();

            productDto.setStatus(ProductStatus.판매중);
            productDto.setCategory(category);
            productDto.setCustomer(customer);
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
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        try {
            List<Product> products = productRepository.findByCategory(category,ProductStatus.판매중);

            if(products.isEmpty()) return chatBotExceptionResponse.createException("등록된 상품이 없습니다.");

            Carousel<BasicCard> carousel = new Carousel<>();

            products.forEach(product -> {
                BasicCard basicCard = new BasicCard();
                Extra extra = new Extra();

                ProductStatus productStatus = product.getStatus();
                String productName = product.getName();
                String productPrice = StringUtils.formatPrice(product.getPrice());
                String productDescription = StringUtils.formatProductInfo(productPrice,productStatus);
                String thumbnailImageUrl = product.getProductImages().getImageUrl().get(0);
                String productId = String.valueOf(product.getId());


                basicCard.setThumbnail(thumbnailImageUrl);
                basicCard.setTitle(productName);
                basicCard.setDescription(productDescription);
                extra.addProductId(productId);
                basicCard.setBlockButton("상세보기",BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(),extra);
                carousel.addComponent(basicCard);
            });

            chatBotResponse.addCarousel(carousel);
            chatBotResponse.addQuickButton(ButtonName.이전으로,BlockId.PRODUCT_GET_CATEGORIES.getBlockId());

            return chatBotResponse;
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
    public Response getProductsByUserKey(String userKey) {
        try {
            List<Product> products = productRepository.findByUserKey(userKey);

            if(products.isEmpty()) return chatBotExceptionResponse.createException("등록된 상품이 없습니다");

            return getCustomerProductsSuccessChatBotResponse(products);
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

            String productUserKey = product.getCustomer().getUserKey();
            String productName = product.getName();
            String productDescription = product.getProductDetailDescription();
            ProductStatus status = product.getStatus();
            List<String> imageUrls = product.getProductImages().getImageUrl();

            return getProductProfileSuccessChatBotResponse(userKey, productUserKey, imageUrls,productName,productDescription,productId,status);
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

    private ChatBotResponse createStatusQuickButtons(String userKey, String productUserKey, ChatBotResponse chatBotResponse, ProductStatus status, Extra extra){

        if(productUserKey.equals(userKey)) switch (status){
            case 판매중:
                chatBotResponse.addQuickButton(ButtonName.숨김,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.예약,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.삭제,BlockId.PRODUCT_DELETE.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.판매완료,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
                return chatBotResponse;
            case 숨김:
                chatBotResponse.addQuickButton(ButtonName.판매중,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.삭제,BlockId.PRODUCT_DELETE.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
                return chatBotResponse;
            case 예약:
                chatBotResponse.addQuickButton(ButtonName.예약취소,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.판매완료,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
                return chatBotResponse;
            default:
                chatBotResponse.addQuickButton(ButtonName.삭제,BlockId.PRODUCT_DELETE.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
                return chatBotResponse;
        }

        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse updateStatusSuccessChatBotResponse(ProductStatus productStatus){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 "+productStatus+" 상태로 변경하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse deleteProductSuccessChatBotResponse(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 정상적으로 삭제하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse verificationCustomerSuccessChatBotResponse(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 등록하시겠습니까?");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.등록하기, BlockId.PRODUCT_ADD_INFO.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse getProductProfileSuccessChatBotResponse(String userKey, String productUserKey, List<String> imageUrls, String productName, String productDescription, String productId, ProductStatus status){
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Extra extra = new Extra();

        Carousel<BasicCard> carouselImage = createCarouselImage(imageUrls);
        extra.addProductId(productId);

        chatBotResponse.addCarousel(carouselImage);
        chatBotResponse.addTextCard(productName,productDescription);
        chatBotResponse = createStatusQuickButtons(userKey, productUserKey, chatBotResponse, status, extra);
        return chatBotResponse;
    }

    private ChatBotResponse getCustomerProductsSuccessChatBotResponse(List<Product> products){
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<BasicCard> carousel = new Carousel<>();

        products.forEach(product -> {
            BasicCard basicCard = new BasicCard();
            Extra extra = new Extra();

            ProductStatus productStatus = product.getStatus();
            String productName = product.getName();
            String productPrice = StringUtils.formatPrice(product.getPrice());
            String productDescription = StringUtils.formatProductInfo(productPrice,productStatus);
            String thumbnailImageUrl = product.getProductImages().getImageUrl().get(0);
            String productId = String.valueOf(product.getId());


            basicCard.setThumbnail(thumbnailImageUrl);
            basicCard.setTitle(productName);
            basicCard.setDescription(productDescription);
            extra.addProductId(productId);
            basicCard.setBlockButton("상세보기",BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(),extra);
            carousel.addComponent(basicCard);
        });

        chatBotResponse.addCarousel(carousel);
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }


    private ChatBotResponse getProductInfoPreviewSuccessChatBotResponse(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl){
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Context productContext = new Context("product",1,600);
        productContext.addParam("productCategory",category);
        //productContext.addParam("accessToken","테스트");

        productDescription = StringUtils.formatProductDetail(category,productPrice,productDescription,tradingLocation,kakaoOpenChatUrl);

        Carousel<BasicCard> carouselImage = createCarouselImage(imageUrls);
        chatBotResponse.addCarousel(carouselImage);
        chatBotResponse.addTextCard(productName,productDescription);
        chatBotResponse.addQuickButton(ButtonName.취소,BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.등록,BlockId.PRODUCT_ADD.getBlockId());
        chatBotResponse.addContext(productContext);
        return chatBotResponse;
    }

    private ChatBotResponse addProductSuccessChatBotResponse(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 정상적으로 등록하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse getCategoryChatBotResponse(BlockId nextBlockId){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        List<Category> categories = Arrays.stream(Category.values()).collect(Collectors.toList());

        chatBotResponse.addSimpleText("카테고리를 선택해주세요.");

        categories.forEach(category -> {
            Extra extra = new Extra();
            extra.addChoiceParam(category.getValue());
            chatBotResponse.addQuickButton(category,nextBlockId.getBlockId(),extra);
        });

        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }
}
