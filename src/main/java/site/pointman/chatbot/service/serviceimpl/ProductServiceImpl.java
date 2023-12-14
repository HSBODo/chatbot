package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.BlockId;
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

import java.util.List;
import java.util.Optional;

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
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            final Long PRODUCT_ID = NumberUtils.createProductId();

            String userKey = chatBotRequest.getUserKey();
            List<String> imgUrlList = chatBotRequest.getProductImages();

            if(!customerService.isCustomer(chatBotRequest)){
                return exceptionResponse.notCustomerException();
            }

            Customer customer = customerRepository.findByCustomer(userKey).get();

            ProductDto productDto = chatBotRequest.createProductDto(customer);
            ProductImageDto productImageDto = s3FileService.uploadProductImage(imgUrlList, userKey,productDto.getName());

            productDto.setStatus(ProductStatus.판매중);
            productDto.setId(PRODUCT_ID);

            productRepository.addProduct(productDto,productImageDto);

            chatBotResponse.addSimpleText("상품을 정상적으로 등록하셨습니다.");
            chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());

            return chatBotResponse;
        }catch (Exception e){
            return  exceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse createProductInfoPreview(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            Context productContext = new Context("product",1,600);

            String productName = chatBotRequest.getProductName();
            String productDescription = formatPreviewProductDescription(chatBotRequest);

            List<String> imageUrls =chatBotRequest.getProductImages();

            Carousel<BasicCard> carouselImage = createCarouselImage(imageUrls);

            //productContext.addParam("accessToken","테스트");

            chatBotResponse.addCarousel(carouselImage);
            chatBotResponse.addTextCard(productName,productDescription);
            chatBotResponse.addQuickButton("취소",BlockId.MAIN.getBlockId());
            chatBotResponse.addQuickButton("등록",BlockId.PRODUCT_ADD.getBlockId());
            chatBotResponse.addContext(productContext);

            return chatBotResponse;
        }catch (Exception e){
            return exceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getCustomerProducts(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            Carousel<BasicCard> carousel = new Carousel<>();
            String userKey = chatBotRequest.getUserKey();

            if(!customerService.isCustomer(chatBotRequest)){
                return exceptionResponse.notCustomerException();
            }
            List<Product> products = productRepository.findByUserKey(userKey);

            if(products.isEmpty()){
                return exceptionResponse.createException("등록된 상품이 없습니다");
            }

            products.forEach(product -> {
                BasicCard basicCard = new BasicCard();
                Extra extra = new Extra();

                String thumbnailImageUrl = product.getProductImages().getImageUrl().get(0);
                String productPrice = String.valueOf(product.getPrice());
                ProductStatus productStatus = product.getStatus();

                String productName = product.getName();
                String productDescription = StringUtils.formatProductInfo(productPrice,productStatus);

                String productId = String.valueOf(product.getId());
                extra.addProductId(productId);

                basicCard.setThumbnail(thumbnailImageUrl);
                basicCard.setTitle(productName);
                basicCard.setDescription(productDescription);
                basicCard.setBlockButton("상세보기",BlockId.CUSTOMER_GET_PRODUCT_DETAIL.getBlockId(),extra);
                carousel.addComponent(basicCard);
            });

            chatBotResponse.addCarousel(carousel);
            chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());

            return chatBotResponse;
        }catch (Exception e){
            return exceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse getCustomerProductDetail(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            final Long productId = Long.parseLong(chatBotRequest.getAction().getClientExtra().getProductId());

            Extra extra = new Extra();
            extra.addProductId(String.valueOf(productId));

            if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();

            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);
            if(mayBeProduct.isEmpty()) return exceptionResponse.createException("상품이 존재하지 않습니다.");

            Product product = mayBeProduct.get();

            ProductStatus status = product.getStatus();
            String productName = product.getName();
            String productDescription = formatProductDetailDescription(product);
            List<String> imageUrls = product.getProductImages().getImageUrl();

            Carousel<BasicCard> carouselImage = createCarouselImage(imageUrls);

            chatBotResponse.addCarousel(carouselImage);
            chatBotResponse.addTextCard(productName,productDescription);

            if(status.equals(ProductStatus.판매중)){
                chatBotResponse.addQuickButton("숨김",BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton("예약",BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton("판매완료",BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
            }
            if(status.equals(ProductStatus.숨김)){
                chatBotResponse.addQuickButton("판매중",BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
            }
            if(status.equals(ProductStatus.예약)){
                chatBotResponse.addQuickButton("예약취소",BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                chatBotResponse.addQuickButton("이전으로",BlockId.CUSTOMER_GET_PRODUCTS.getBlockId());
                chatBotResponse.addQuickButton("판매완료",BlockId.PRODUCT_UPDATE_STATUS.getBlockId(),extra);
                return chatBotResponse;
            }

            chatBotResponse.addQuickButton("삭제",BlockId.PRODUCT_DELETE.getBlockId(),extra);
            chatBotResponse.addQuickButton("이전으로",BlockId.CUSTOMER_GET_PRODUCTS.getBlockId());

            return chatBotResponse;
        }catch (Exception e){
            return exceptionResponse.createException();
        }

    }

    @Override
    public ChatBotResponse updateProductStatus(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        try {
            final Long productId = Long.parseLong(chatBotRequest.getAction().getClientExtra().getProductId());

            String utterance = chatBotRequest.getUtterance();

            if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();

            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);
            if(mayBeProduct.isEmpty())  return exceptionResponse.createException("상품이 존재하지 않습니다.");

            if(utterance.equals(ProductStatus.숨김.name())){
                productRepository.updateStatus(productId,ProductStatus.숨김);
                chatBotResponse.addSimpleText("상품을 숨기기 상태로 변경하였습니다.");
                chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());
                return chatBotResponse;
            }

            if(utterance.equals(ProductStatus.판매중.name()) ||utterance.equals(ProductStatus.예약취소.name())){
                productRepository.updateStatus(productId,ProductStatus.판매중);
                chatBotResponse.addSimpleText("상품 판매 상태로 변경하였습니다.");
                chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());
                return chatBotResponse;
            }

            if(utterance.equals(ProductStatus.예약.name())){
                productRepository.updateStatus(productId,ProductStatus.예약);
                chatBotResponse.addSimpleText("상품을 예약 상태로 변경하였습니다.");
                chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());
                return chatBotResponse;
            }

            if(utterance.equals(ProductStatus.판매완료.name())){
                productRepository.updateStatus(productId,ProductStatus.판매완료);
                chatBotResponse.addSimpleText("상품을 판매완료 상태로 변경하였습니다.");
                chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());
                return chatBotResponse;
            }

            return exceptionResponse.createException("상태변경을 실패하였습니다.");
        }catch (Exception e){
            return exceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse deleteProduct(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        try {
            final Long productId = Long.parseLong(chatBotRequest.getAction().getClientExtra().getProductId());

            String utterance = chatBotRequest.getUtterance();

            if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();

            Optional<Product> mayBeProduct = productRepository.findByProductId(productId);
            if(mayBeProduct.isEmpty())   return exceptionResponse.createException("상품이 존재하지 않습니다.");

            if(utterance.equals("삭제")){
                productRepository.deleteProduct(productId);
                chatBotResponse.addSimpleText("상품을 정상적으로 삭제하였습니다.");
                chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());
                return chatBotResponse;
            }

            return exceptionResponse.createException("상품 삭제를 실패하였습니다.");
        }catch (Exception e){
            return exceptionResponse.createException();
        }
    }

    @Override
    public ChatBotResponse validationCustomer(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        if(!customerService.isCustomer(chatBotRequest)) return exceptionResponse.notCustomerException();

        chatBotResponse.addSimpleText("상품을 등록하시겠습니까?");
        chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton("등록하기", BlockId.PRODUCT_ADD_INFO.getBlockId());

        return chatBotResponse;
    }

    private String formatPreviewProductDescription(ChatBotRequest chatBotRequest){
        String productDescription = chatBotRequest.getProductDescription();
        String productPrice = chatBotRequest.getProductPrice();
        String tradingLocation = chatBotRequest.getTradingLocation();
        String kakaoOpenChatUrl = chatBotRequest.getKakaoOpenChatUrl();

        productDescription = StringUtils.formatProductDetail(productPrice,productDescription,tradingLocation,kakaoOpenChatUrl);
        return productDescription;
    }

    private String formatProductDetailDescription(Product product){
        ProductStatus productStatus = product.getStatus();
        String productDescription = product.getDescription();
        String productPrice = String.valueOf(product.getPrice());
        String tradingLocation = product.getTradingLocation();
        String kakaoOpenChatUrl = product.getKakaoOpenChatUrl();
        String productCreateDate = product.getCreateDate();

        productDescription = StringUtils.formatProductDetail(productStatus,productPrice,productDescription,tradingLocation,kakaoOpenChatUrl,productCreateDate);
        return productDescription;
    }

    private Carousel<BasicCard> createCarouselImage(List<String> imageUrls){
        Carousel<BasicCard> basicCardCarousel = new Carousel<>();
        imageUrls.forEach(imageUrl -> {
            BasicCard basicCard = new BasicCard();
            basicCard.setThumbnail(imageUrl);
            basicCardCarousel.addComponent(basicCard);
        });
        return basicCardCarousel;
    }
}
