package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.BlockId;
import site.pointman.chatbot.constant.ButtonName;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.customer.Customer;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.ExceptionResponse;
import site.pointman.chatbot.domain.response.property.Context;
import site.pointman.chatbot.domain.response.property.common.Extra;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.CustomerRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.CustomerService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.service.S3FileService;
import site.pointman.chatbot.utill.NumberUtils;
import site.pointman.chatbot.utill.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    CustomerService customerService;
    S3FileService s3FileService;

    ProductRepository productRepository;
    CustomerRepository customerRepository;

    public ProductServiceImpl(CustomerService customerService, S3FileService s3FileService, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.s3FileService = s3FileService;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ChatBotResponse addProduct(ChatBotRequest chatBotRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            final Long PRODUCT_ID = NumberUtils.createProductId();
            final String USER_KEY = chatBotRequest.getUserKey();
            final List<String> IMAGE_URLS = chatBotRequest.getProductImages();
            final Category productCategory = Category.getCategory(chatBotRequest.getContexts().get(0).getParams().get("productCategory").getValue());


            if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();
            Customer customer = customerRepository.findByCustomer(USER_KEY).get();

            ProductDto productDto = chatBotRequest.createProductDto();
            String productName = productDto.getName();

            productDto.setStatus(ProductStatus.판매중);
            productDto.setCategory(productCategory);
            productDto.setCustomer(customer);
            productDto.setId(PRODUCT_ID);

            ProductImageDto productImageDto = s3FileService.uploadProductImage(IMAGE_URLS, USER_KEY,productName);

            productRepository.addProduct(productDto,productImageDto);

            return addProductSuccessResponse();
        }catch (Exception e){
            return  exceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse addCategory(ChatBotRequest chatBotRequest) {
        return selectCategoryResponse(BlockId.PRODUCT_PROFILE_PREVIEW);
    }

    @Override
    public ChatBotResponse getProductInfoPreview(ChatBotRequest chatBotRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            List<String> imageUrls = chatBotRequest.getProductImages();
            String productName = chatBotRequest.getProductName();
            String productDescription = chatBotRequest.getProductDescription();
            String productPrice = StringUtils.formatPrice(Integer.parseInt(chatBotRequest.getProductPrice()));
            String tradingLocation = chatBotRequest.getTradingLocation();
            String kakaoOpenChatUrl = chatBotRequest.getKakaoOpenChatUrl();
            String category = chatBotRequest.getChoiceParam();

            return getProductInfoPreviewSuccessResponse(imageUrls, category, productName,productDescription,productPrice,tradingLocation,kakaoOpenChatUrl);
        }catch (Exception e){
            return exceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getCustomerProducts(ChatBotRequest chatBotRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            String userKey = chatBotRequest.getUserKey();

            if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();

            List<Product> products = productRepository.findByUserKey(userKey);
            if(products.isEmpty()) return exceptionResponse.createException("등록된 상품이 없습니다");

            return getCustomerProductsSuccessResponse(products);
        }catch (Exception e){
            return exceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getCustomerProductDetail(ChatBotRequest chatBotRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            final Long PRODUCT_ID = Long.parseLong(chatBotRequest.getProductId());

            if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();

            Optional<Product> mayBeProduct = productRepository.findByProductId(PRODUCT_ID);
            if(mayBeProduct.isEmpty()) return exceptionResponse.createException("상품이 존재하지 않습니다.");
            Product product = mayBeProduct.get();

            String productName = product.getName();
            String productDescription = product.getProductDetailDescription();
            ProductStatus status = product.getStatus();
            List<String> imageUrls = product.getProductImages().getImageUrl();

            return getCustomerProductDetailSuccessResponse(imageUrls,productName,productDescription,String.valueOf(PRODUCT_ID),status);
        }catch (Exception e){
            return exceptionResponse.createException();
        }

    }

    @Override
    public ChatBotResponse updateProductStatus(ChatBotRequest chatBotRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            final Long PRODUCT_ID = Long.parseLong(chatBotRequest.getProductId());
            final String UTTERANCE = chatBotRequest.getUtterance();
            final ProductStatus PRODUCT_STATUS = ProductStatus.getProductStatus(UTTERANCE);

            if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();

            Optional<Product> mayBeProduct = productRepository.findByProductId(PRODUCT_ID);
            if(mayBeProduct.isEmpty()) return exceptionResponse.createException("상품이 존재하지 않습니다.");

            productRepository.updateStatus(PRODUCT_ID,PRODUCT_STATUS);

            return updateStatusSuccessResponse(PRODUCT_STATUS);
        }catch (Exception e){
            return exceptionResponse.createException("상태변경을 실패하였습니다.");
        }
    }

    @Override
    public ChatBotResponse deleteProduct(ChatBotRequest chatBotRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            final Long PRODUCT_ID = Long.parseLong(chatBotRequest.getProductId());
            final String UTTERANCE = chatBotRequest.getUtterance();

            if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();

            Optional<Product> mayBeProduct = productRepository.findByProductId(PRODUCT_ID);
            if(mayBeProduct.isEmpty()) return exceptionResponse.createException("상품이 존재하지 않습니다.");

            if(ProductStatus.삭제.name().equals(UTTERANCE)){
                productRepository.deleteProduct(PRODUCT_ID);
                return deleteProductSuccessResponse();
            }

            return exceptionResponse.createException("상품 삭제를 실패하였습니다.");
        }catch (Exception e){
            return exceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse validationCustomer(ChatBotRequest chatBotRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();

        return validationCustomerSuccessResponse();
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

    private ChatBotResponse createStatusQuickButtons(ChatBotResponse chatBotResponse, ProductStatus status, Extra extra){
        switch (status){
            case 판매중:
                chatBotResponse.addQuickButton(ButtonName.숨김,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.예약,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.삭제,BlockId.PRODUCT_DELETE.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.판매완료,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.이전으로,BlockId.CUSTOMER_GET_PRODUCTS.getBlockId());
                return chatBotResponse;
            case 숨김:
                chatBotResponse.addQuickButton(ButtonName.판매중,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.삭제,BlockId.PRODUCT_DELETE.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.이전으로,BlockId.CUSTOMER_GET_PRODUCTS.getBlockId());
                return chatBotResponse;
            case 예약:
                chatBotResponse.addQuickButton(ButtonName.예약취소,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.판매완료,BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.이전으로,BlockId.CUSTOMER_GET_PRODUCTS.getBlockId());
                return chatBotResponse;
            default:
                chatBotResponse.addQuickButton(ButtonName.삭제,BlockId.PRODUCT_DELETE.getBlockId(),extra);
                chatBotResponse.addQuickButton(ButtonName.이전으로,BlockId.CUSTOMER_GET_PRODUCTS.getBlockId());
                return chatBotResponse;
        }
    }

    private ChatBotResponse updateStatusSuccessResponse(ProductStatus productStatus){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 "+productStatus+" 상태로 변경하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse deleteProductSuccessResponse(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 정상적으로 삭제하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse validationCustomerSuccessResponse(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 등록하시겠습니까?");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton(ButtonName.등록하기, BlockId.PRODUCT_ADD_INFO.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse getCustomerProductDetailSuccessResponse(List<String> imageUrls, String productName, String productDescription, String productId, ProductStatus status){
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Extra extra = new Extra();

        Carousel<BasicCard> carouselImage = createCarouselImage(imageUrls);
        extra.addProductId(productId);

        chatBotResponse.addCarousel(carouselImage);
        chatBotResponse.addTextCard(productName,productDescription);
        chatBotResponse = createStatusQuickButtons(chatBotResponse, status, extra);
        return chatBotResponse;
    }

    private ChatBotResponse getCustomerProductsSuccessResponse(List<Product> products){
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


    private ChatBotResponse getProductInfoPreviewSuccessResponse(List<String> imageUrls, String category, String productName, String productDescription, String productPrice, String tradingLocation, String kakaoOpenChatUrl){
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

    private ChatBotResponse addProductSuccessResponse(){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        chatBotResponse.addSimpleText("상품을 정상적으로 등록하였습니다.");
        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }

    private ChatBotResponse selectCategoryResponse(BlockId nextBlockId){
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        List<Category> categories = Arrays.stream(Category.values()).collect(Collectors.toList());

        chatBotResponse.addSimpleText("상품의 카테고리를 선택해주세요.");

        categories.forEach(category -> {
            Extra extra = new Extra();
            extra.addChoiceParam(category.getValue());
            chatBotResponse.addQuickButton(category,nextBlockId.getBlockId(),extra);
        });

        chatBotResponse.addQuickButton(ButtonName.처음으로,BlockId.MAIN.getBlockId());
        return chatBotResponse;
    }
}
