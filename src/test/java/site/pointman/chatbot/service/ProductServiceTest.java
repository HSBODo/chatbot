package site.pointman.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.constant.Category;
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.utill.NumberUtils;
import site.pointman.chatbot.utill.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest

class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    private ObjectMapper mapper = new ObjectMapper();
    private ChatBotRequest chatBotRequest;
    private String userKey;
    private String productId;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        userKey = "QFJSyeIZbO77";
        productId = "5942";
    }

    @Test
    @Transactional
    void validationCustomer() throws Exception{
        String body="{\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"intent\":{\"id\":\"657bc60c11c58311fd9c9fa4\",\"name\":\"테스트\",\"extra\":{\"reason\":{\"code\":1,\"message\":\"OK\"}}},\"action\":{\"id\":\"657bc62306b53a111f7c354e\",\"name\":\"테스트_Json\",\"params\":{},\"detailParams\":{},\"clientExtra\":{}},\"userRequest\":{\"block\":{\"id\":\"657bc60c11c58311fd9c9fa4\",\"name\":\"테스트\"},\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":true,\"plusfriendUserKey\":\""+userKey+"\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}},\"utterance\":\"테스트\",\"params\":{\"surface\":\"Kakaotalk.plusfriend\"},\"lang\":\"ko\",\"timezone\":\"Asia/Seoul\"},\"contexts\":[]}\n";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .readValue(body, ChatBotRequest.class);

        ChatBotResponse chatBotResponse = productService.verificationCustomerSuccessResponse();
        String text = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();
        Assertions.assertThat(text).isEqualTo("상품을 등록하시겠습니까?");
    }

    @Test
    @Transactional
    void createProductInfoPreview() throws Exception{
        //give
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://secure.kakaocdn.net/dna/eAWeuk/K6beei5MQR/XXX/img_org.jpg?credential");

        String productName = "테스트상품명";
        String productDescription = "테스트 상품 설명";
        String productPrice = "10000";
        String tradingLocation = "서울특별시";
        String kakaoOpenChatUrl = "https://open.kakao.com/o";
        String category = Category.스포츠_레저.getValue();

        //when
        ChatBotResponse chatBotResponse = productService.getProductInfoPreview(
                imageUrls,
                productName,
                productDescription,
                productPrice,
                tradingLocation,
                kakaoOpenChatUrl,
                category
                );
        String text = chatBotResponse.getTemplate().getOutputs().get(1).getTextCard().getTitle();

        //then
        Assertions.assertThat(text).isEqualTo("상품이름");
    }

    @Test
    @Transactional
    void addProduct() throws JsonProcessingException {
        //give
        Long productId = NumberUtils.createProductId();
        String userKey = chatBotRequest.getUserKey();
        List<String> imageUrls = chatBotRequest.getProductImages();
        String productCategory = chatBotRequest.getContexts().get(0).getParams().get("productCategory").getValue();
        ProductDto productDto = chatBotRequest.createProductDto();

        //when
        ChatBotResponse chatBotResponse = productService.addProduct(productDto,productId,userKey,imageUrls,productCategory);
        String text = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(text).isEqualTo("상품을 정상적으로 등록하셨습니다.");
    }


    @Test
    @Transactional
    void getCustomerProducts() throws JsonProcessingException {
        //give
        List<Product> products = productRepository.findByUserKey(userKey);

        //when
        ChatBotResponse customerProducts = productService.getProductsByUserKey(userKey);
        int customerProductsSize = customerProducts.getTemplate().getOutputs().get(0).getCarousel().getItems().size();

        //then
        Assertions.assertThat(customerProductsSize).isEqualTo(products.size());
    }

    @Test
    @Transactional
    void getCustomerProductDetail() throws JsonProcessingException {
        //give
        Product product = productRepository.findByProductId(Long.parseLong(productId)).get();

        //when
        ChatBotResponse customerProductDetail = productService.getProductProfile(productId,userKey);
        String title = customerProductDetail.getTemplate().getOutputs().get(1).getTextCard().getTitle();

        //then
        Assertions.assertThat(title).isEqualTo(product.getName());
    }

    @Test
    void updateProductStatus() throws JsonProcessingException {
        //give
        String utterance =  ProductStatus.판매중.name();;

        //when
        ChatBotResponse chatBotResponse = productService.updateProductStatus(productId,utterance);
        String text = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(text).isEqualTo("상품을 "+utterance+" 상태로 변경하였습니다.");
    }

    @Test
    @Transactional
    void deleteProduct() throws JsonProcessingException {
        //give
        String utterance = ProductStatus.삭제.name();

        //when
        ChatBotResponse chatBotResponse = productService.deleteProduct(productId,utterance);
        String text = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(text).isEqualTo("상품을 정상적으로 삭제하였습니다.");

    }
}