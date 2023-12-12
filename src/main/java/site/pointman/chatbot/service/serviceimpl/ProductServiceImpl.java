package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import org.springframework.stereotype.Service;

import site.pointman.chatbot.domain.response.ValidationResponse;
import site.pointman.chatbot.dto.exception.ExceptionResponseDto;
import site.pointman.chatbot.dto.oauthtoken.OAuthTokenDto;
import site.pointman.chatbot.dto.product.ProductImgDto;
import site.pointman.chatbot.dto.product.ProductListDto;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.request.propery.ProductImg;
import site.pointman.chatbot.dto.response.ResponseDto;
import site.pointman.chatbot.dto.response.property.Context;
import site.pointman.chatbot.dto.response.property.common.Buttons;
import site.pointman.chatbot.dto.response.property.common.Thumbnail;
import site.pointman.chatbot.dto.response.property.components.BasicCard;
import site.pointman.chatbot.dto.response.property.components.Carousel;
import site.pointman.chatbot.service.AuthService;
import site.pointman.chatbot.service.ProductService;
import site.pointman.chatbot.service.CustomerService;
import site.pointman.chatbot.utill.BlockId;
import site.pointman.chatbot.utill.HttpUtils;
import site.pointman.chatbot.utill.NumberUtils;
import site.pointman.chatbot.utill.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final String KAKAO_OPEN_CHAT_URL_REQUIRED = "https://open.kakao.com/o";


    AuthService authService;
    CustomerService customerService;

    public ProductServiceImpl(AuthService authService, CustomerService customerService) {
        this.authService = authService;
        this.customerService = customerService;
    }

    HttpUtils httpUtils;

    @Override
    public ProductListDto getProducts(OAuthTokenDto tokenDto) {
        ProductListDto productDto = null;

        MediaType mediaType = MediaType.parse("application/json");
        String productsUrl = "https://api.commerce.naver.com/external/v1/products/search";

        Map<String,Object> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+tokenDto.getAccessToken());
        headers.put("Content-type","application/json");

        Map<String,Object> body = new HashMap<>();
        body.put("searchKeywordType","SELLER_CODE");
//      body.put("channelProductNos","");
//      body.put("originProductNos","");
        body.put("sellerManagementCode","1");
        body.put("productStatusTypes","SALE");
        body.put("page","1");
        body.put("size","10");
        body.put("orderType","NO");
        body.put("periodType","SALE_START_DAY");
        body.put("fromDate","2023-01-01");
        body.put("toDate","2023-10-03");

        String httpResponse = httpUtils.post(productsUrl,headers, body, mediaType);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            productDto = mapper.readValue(httpResponse, ProductListDto.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        // 상품 이미지 세팅
        for(int i = 0 ; i < productDto.getProductListDto().size(); i++){
            String url = "https://api.commerce.naver.com/external/v2/products/origin-products/"+productDto.getProduct(i).getOriginProductNo();
            httpResponse = httpUtils.get(url,headers);
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
                ProductImgDto productImgDto = mapper.readValue(httpResponse, ProductImgDto.class);
                productDto.getProduct(i).setImageUrl(productImgDto.getImageUrl());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        return productDto;
    }


    @Override
    public ResponseDto createProductInfoPreview(ChatBotRequest chatBotRequest) {
        ResponseDto responseDto = new ResponseDto();
        Carousel<BasicCard> basicCardCarousel = new Carousel<>();
        Context productContext = new Context("product",1,600);

        String accessToken = chatBotRequest.getAccessToken();
        String productName = chatBotRequest.getProductName();
        String productDescription = formatProductDescription(chatBotRequest);

        ProductImg productImg = chatBotRequest.getProductImg();
        List<String> imgUrlList = productImg.getImgUrlList();

        imgUrlList.forEach(imgUrl -> {
            BasicCard basicCard = new BasicCard();
            Thumbnail thumbnail = new Thumbnail(imgUrl,null,true);
            basicCard.setThumbnail(thumbnail);
            basicCardCarousel.addComponent(basicCard);
        });

        productContext.addParam("accessToken","테스트");

        responseDto.addCarousel(basicCardCarousel);
        responseDto.addTextCard(productName,productDescription);
        responseDto.addQuickButton("취소",BlockId.MAIN.getBlockId());
        responseDto.addQuickButton("등록",BlockId.PRODUCT_ADD.getBlockId());
        responseDto.addContext(productContext);

        return responseDto;
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
    public ResponseDto validationCustomer(ChatBotRequest chatBotRequest) {
        ResponseDto responseDto = new ResponseDto();

        if(!customerService.isCustomer(chatBotRequest)){
             ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto();
             exceptionResponseDto.addNotCustomerException();
            return exceptionResponseDto;
        }

        responseDto.addSimpleText("상품을 등록하시겠습니까?");
        responseDto.addQuickButton("메인으로",BlockId.MAIN.getBlockId());
        responseDto.addQuickButton("등록하기", BlockId.PRODUCT_ADD_INFO.getBlockId());

        return responseDto;
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
