package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.customer.Customer;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.ProductImage;
import site.pointman.chatbot.domain.response.ValidationResponse;
import site.pointman.chatbot.domain.response.ExceptionResponse;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.request.propery.ProductImg;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.property.Context;
import site.pointman.chatbot.domain.response.property.common.Thumbnail;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.Carousel;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.ProductImageDto;
import site.pointman.chatbot.repository.CustomerRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.service.CustomerService;
import site.pointman.chatbot.constant.BlockId;
import site.pointman.chatbot.service.S3FileService;
import site.pointman.chatbot.utill.HttpUtils;
import site.pointman.chatbot.utill.NumberUtils;
import site.pointman.chatbot.utill.StringUtils;
import site.pointman.chatbot.utill.UrlResourceDownloader;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final String KAKAO_OPEN_CHAT_URL_REQUIRED = "https://open.kakao.com/o";

    AuthService authService;
    CustomerService customerService;
    S3FileService s3FileService;
    ProductRepository productRepository;
    CustomerRepository customerRepository;

    public ProductServiceImpl(AuthService authService, CustomerService customerService, S3FileService s3FileService, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.authService = authService;
        this.customerService = customerService;
        this.s3FileService = s3FileService;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ChatBotResponse addProduct(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        String userKey = chatBotRequest.getUserKey();

        Optional<Customer> maybeCustomer = customerRepository.findByCustomer(userKey);

        if(maybeCustomer.isEmpty()){
            ExceptionResponse exceptionResponseDto = new ExceptionResponse();
            exceptionResponseDto.addNotCustomerException();
            return exceptionResponseDto;
        }

        Customer customer = maybeCustomer.get();
        ProductDto productDto = chatBotRequest.createProductDto(customer);
        productRepository.save(productDto);

        List<String> imgUrlList =chatBotRequest.getProductImg().getImgUrlList();
        saveProductImage(imgUrlList,productDto);

        chatBotResponse.addSimpleText("상품을 정상적으로 등록하셨습니다.");
        chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());

        return chatBotResponse;
    }

    private void saveProductImage(List<String> imgUrlList,ProductDto productDto){
        final String ext = ".jpg";
        final String dir = "image";
        ProductImageDto productImageDto = new ProductImageDto();

        imgUrlList.forEach(imgUrl -> {
            final String fileName = StringUtils.createImgFileName(productDto);
            UrlResourceDownloader urlResourceDownloader = new UrlResourceDownloader( dir+"/"+fileName+ext);
            urlResourceDownloader.download(imgUrl);
            File file = new File(dir+"/"+fileName+ext);
            String uploadReturnUrl = s3FileService.upload(file, dir);
            productImageDto.getImageUrl().add(uploadReturnUrl);
        });

        productRepository.productImageSave(productImageDto);
    }

    @Override
    public ChatBotResponse createProductInfoPreview(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();
        Carousel<BasicCard> basicCardCarousel = new Carousel<>();
        Context productContext = new Context("product",1,600);

        String accessToken = chatBotRequest.getAccessToken();
        String productName = chatBotRequest.getProductName();
        String productDescription = formatProductDescription(chatBotRequest);

        List<String> imgUrlList =chatBotRequest.getProductImg().getImgUrlList();

        imgUrlList.forEach(imgUrl -> {
            BasicCard basicCard = new BasicCard();
            Thumbnail thumbnail = new Thumbnail(imgUrl,null,true);
            basicCard.setThumbnail(thumbnail);
            basicCardCarousel.addComponent(basicCard);
        });

        productContext.addParam("accessToken","테스트");

        chatBotResponse.addCarousel(basicCardCarousel);
        chatBotResponse.addTextCard(productName,productDescription);
        chatBotResponse.addQuickButton("취소",BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton("등록",BlockId.PRODUCT_ADD.getBlockId());
        chatBotResponse.addContext(productContext);

        return chatBotResponse;
    }

    private String formatProductDescription(ChatBotRequest chatBotRequest){
        String productDescription = chatBotRequest.getProductDescription();
        String productPrice = chatBotRequest.getProductPrice();
        String tradingLocation = chatBotRequest.getTradingLocation();
        String kakaoOpenChatUrl = chatBotRequest.getKakaoOpenChatUrl();

        productDescription = StringUtils.formatProductDetail(productPrice,productDescription,tradingLocation,kakaoOpenChatUrl);
        return productDescription;
    }

    @Override
    public ChatBotResponse validationCustomer(ChatBotRequest chatBotRequest) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        if(!customerService.isCustomer(chatBotRequest)){
             ExceptionResponse exceptionResponseDto = new ExceptionResponse();
             exceptionResponseDto.addNotCustomerException();
            return exceptionResponseDto;
        }

        chatBotResponse.addSimpleText("상품을 등록하시겠습니까?");
        chatBotResponse.addQuickButton("메인으로",BlockId.MAIN.getBlockId());
        chatBotResponse.addQuickButton("등록하기", BlockId.PRODUCT_ADD_INFO.getBlockId());

        return chatBotResponse;
    }

    @Override
    public ValidationResponse validationProductName(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String productName = chatBotRequest.getValidationData();
        if(productName.length()>30){
            validationResponse.validationFail();
            return validationResponse;
        }
        validationResponse.validationSuccess(productName);
        return validationResponse;
    }

    @Override
    public ValidationResponse validationProductPrice(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String productPrice= chatBotRequest.getValidationData();

        if(!NumberUtils.isNumber(productPrice)){
            validationResponse.validationFail();
            return validationResponse;
        }

        validationResponse.validationSuccess(productPrice);
        return validationResponse;
    }

    @Override
    public ValidationResponse validationProductDescription(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String productDescription= chatBotRequest.getValidationData();

        if(productDescription.length()>400){
            validationResponse.validationFail();
            return validationResponse;
        }

        validationResponse.validationSuccess(productDescription);
        return validationResponse;
    }

    @Override
    public ValidationResponse validationKakaoOpenChatUrl(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String kakaoOpenChayUrl= chatBotRequest.getValidationData();

        if(kakaoOpenChayUrl.contains(KAKAO_OPEN_CHAT_URL_REQUIRED)){
            validationResponse.validationSuccess(kakaoOpenChayUrl);
            return validationResponse;
        }

        validationResponse.validationFail();
        return validationResponse;
    }

    @Override
    public ValidationResponse validationTradingLocation(ChatBotRequest chatBotRequest) {
        ValidationResponse validationResponse = new ValidationResponse();
        String tradingLocation= chatBotRequest.getValidationData();

        validationResponse.validationSuccess(tradingLocation);
        return validationResponse;
    }
}
