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
import site.pointman.chatbot.constant.ProductStatus;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.repository.ProductRepository;

import javax.transaction.Transactional;
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
    private String useKey;
    private String productId;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        useKey = "QFJSyeIZbO77";
        productId = "5942";
    }

    @Test
    @Transactional
    void validationCustomer() throws Exception{
        String body="{\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"intent\":{\"id\":\"657bc60c11c58311fd9c9fa4\",\"name\":\"테스트\",\"extra\":{\"reason\":{\"code\":1,\"message\":\"OK\"}}},\"action\":{\"id\":\"657bc62306b53a111f7c354e\",\"name\":\"테스트_Json\",\"params\":{},\"detailParams\":{},\"clientExtra\":{}},\"userRequest\":{\"block\":{\"id\":\"657bc60c11c58311fd9c9fa4\",\"name\":\"테스트\"},\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":true,\"plusfriendUserKey\":\""+useKey+"\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}},\"utterance\":\"테스트\",\"params\":{\"surface\":\"Kakaotalk.plusfriend\"},\"lang\":\"ko\",\"timezone\":\"Asia/Seoul\"},\"contexts\":[]}\n";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .readValue(body, ChatBotRequest.class);

        ChatBotResponse chatBotResponse = productService.validationCustomer(chatBotRequest);
        String text = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();
        Assertions.assertThat(text).isEqualTo("상품을 등록하시겠습니까?");
    }

    @Test
    @Transactional
    void createProductInfoPreview() throws Exception{
        //give
        String body="{\"intent\":{\"id\":\"652a0a9a27e3c4125a33f6eb\",\"name\":\"상품정보 입력 및 미리보기\"},\"userRequest\":{\"params\":{},\"block\":{\"id\":\"652a0a9a27e3c4125a33f6eb\",\"name\":\"상품정보 입력 및 미리보기\"},\"utterance\":\"등록하기\",\"lang\":\"ko\",\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":false,\"plusfriendUserKey\":\"QFJSyeIZbO77\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}}},\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"action\":{\"name\":\"상품_정보입력_미리보기\",\"clientExtra\":{},\"params\":{\"productName\":\"상품이름\",\"productDescription\":\"상품설명\",\"productImg\":\"{\\\"privacyAgreement\\\":\\\"Y\\\",\\\"imageQuantity\\\":\\\"1\\\",\\\"secureUrls\\\":\\\"List(http://secure.kakaocdn.net/dna/eAWeuk/K6beei5MQR/XXX/img_org.jpg?credential\\u003dKq0eSbCrZgKIq51jh41Uf1jLsUh7VWcz\\u0026expires\\u003d1702641400\\u0026allow_ip\\u003d\\u0026allow_referer\\u003d\\u0026signature\\u003ddI2NZTDK5C4LsQ2u8E2gpmazuPM%3D)\\\",\\\"expire\\\":\\\"2023-12-15T11:56:40+0000\\\"}\",\"productPrice\":\"100000\",\"kakaoOpenChatUrl\":\"https://open.kakao.com/o/slfrEtXf\",\"tradingLocation\":\"부천시\"},\"id\":\"652e18c39df46e7601eb27eb\",\"detailParams\":{}},\"contexts\":[]}";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .readValue(body, ChatBotRequest.class);
        //when
        ChatBotResponse chatBotResponse = productService.getProductInfoPreview(chatBotRequest);
        String text = chatBotResponse.getTemplate().getOutputs().get(1).getTextCard().getTitle();

        //then
        Assertions.assertThat(text).isEqualTo("상품이름");
    }

    @Test
    @Transactional
    void addProduct() throws JsonProcessingException {
        //give
        String imageUrl="https://pointman-file-repository.s3.ap-northeast-2.amazonaws.com/image/%EC%98%A8%EB%8F%842.png";
        String body="{\"intent\":{\"id\":\"652e659087e33b27c8ba3a4a\",\"name\":\"상품등록\"},\"userRequest\":{\"params\":{},\"block\":{\"id\":\"652e659087e33b27c8ba3a4a\",\"name\":\"상품등록\"},\"utterance\":\"등록\",\"lang\":\"ko\",\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":false,\"plusfriendUserKey\":\"QFJSyeIZbO77\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}}},\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"action\":{\"name\":\"상품_등록\",\"clientExtra\":{},\"params\":{\"productName\":\"상품이름\",\"productDescription\":\"상품설명\",\"productImg\":\"{\\\"privacyAgreement\\\":\\\"Y\\\",\\\"imageQuantity\\\":\\\"1\\\",\\\"secureUrls\\\":\\\"List("+imageUrl+")\\\",\\\"expire\\\":\\\"2023-12-15T11:56:40+0000\\\"}\",\"productPrice\":\"100000\",\"kakaoOpenChatUrl\":\"https://open.kakao.com/o/slfrEtXf\",\"tradingLocation\":\"부천시\"},\"id\":\"652e66c1b3ba010afdf6b9f2\",\"detailParams\":{}},\"contexts\":[{\"name\":\"product\",\"lifespan\":1,\"ttl\":600,\"params\":{\"productPrice\":{\"value\":\"100000\",\"resolvedValue\":\"100000\"},\"productImg\":{\"value\":\"List("+imageUrl+")\",\"resolvedValue\":\"{\\\"privacyAgreement\\\":\\\"Y\\\",\\\"imageQuantity\\\":\\\"1\\\",\\\"secureUrls\\\":\\\"List("+imageUrl+")\\\",\\\"expire\\\":\\\"2023-12-15T11:56:40+0000\\\"}\"},\"kakaoOpenChatUrl\":{\"value\":\"https://open.kakao.com/o/slfrEtXf\",\"resolvedValue\":\"https://open.kakao.com/o/slfrEtXf\"},\"tradingLocation\":{\"value\":\"부천시\",\"resolvedValue\":\"부천시\"},\"productDescription\":{\"value\":\"상품설명\",\"resolvedValue\":\"상품설명\"},\"productName\":{\"value\":\"상품이름\",\"resolvedValue\":\"상품이름\"}}}]}";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .readValue(body, ChatBotRequest.class);
        //when
        ChatBotResponse chatBotResponse = productService.addProduct(chatBotRequest);
        String text = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(text).isEqualTo("상품을 정상적으로 등록하셨습니다.");
    }


    @Test
    @Transactional
    void getCustomerProducts() throws JsonProcessingException {
        //give
        String body="{\"intent\":{\"id\":\"652a0a9a27e3c4125a33f6eb\",\"name\":\"상품정보 입력 및 미리보기\"},\"userRequest\":{\"params\":{},\"block\":{\"id\":\"652a0a9a27e3c4125a33f6eb\",\"name\":\"상품정보 입력 및 미리보기\"},\"utterance\":\"등록하기\",\"lang\":\"ko\",\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":false,\"plusfriendUserKey\":\""+useKey+"\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}}},\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"action\":{\"name\":\"상품_정보입력_미리보기\",\"clientExtra\":{},\"params\":{\"productName\":\"상품이름\",\"productDescription\":\"상품설명\",\"productImg\":\"{\\\"privacyAgreement\\\":\\\"Y\\\",\\\"imageQuantity\\\":\\\"1\\\",\\\"secureUrls\\\":\\\"List(http://secure.kakaocdn.net/dna/eAWeuk/K6beei5MQR/XXX/img_org.jpg?credential\\u003dKq0eSbCrZgKIq51jh41Uf1jLsUh7VWcz\\u0026expires\\u003d1702641400\\u0026allow_ip\\u003d\\u0026allow_referer\\u003d\\u0026signature\\u003ddI2NZTDK5C4LsQ2u8E2gpmazuPM%3D)\\\",\\\"expire\\\":\\\"2023-12-15T11:56:40+0000\\\"}\",\"productPrice\":\"100000\",\"kakaoOpenChatUrl\":\"https://open.kakao.com/o/slfrEtXf\",\"tradingLocation\":\"부천시\"},\"id\":\"652e18c39df46e7601eb27eb\",\"detailParams\":{}},\"contexts\":[]}";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .readValue(body, ChatBotRequest.class);
        List<Product> products = productRepository.findByUserKey(useKey);
        //when
        ChatBotResponse customerProducts = productService.getCustomerProducts(chatBotRequest);
        int customerProductsSize = customerProducts.getTemplate().getOutputs().get(0).getCarousel().getItems().size();

        //then
        Assertions.assertThat(customerProductsSize).isEqualTo(products.size());
    }

    @Test
    @Transactional
    void getCustomerProductDetail() throws JsonProcessingException {
        //give
        String body="{\"intent\":{\"id\":\"657aa39383cd6b068ef93501\",\"name\":\"회원 등록상품 상세보기 \"},\"userRequest\":{\"params\":{},\"block\":{\"id\":\"657aa39383cd6b068ef93501\",\"name\":\"회원 등록상품 상세보기 \"},\"utterance\":\"상세보기\",\"lang\":\"ko\",\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":false,\"plusfriendUserKey\":\"QFJSyeIZbO77\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}}},\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"action\":{\"name\":\"회원_등록상품_상세보기\",\"clientExtra\":{\"extra\":{\"productId\":\"7810\"},\"productId\":\""+productId+"\"},\"params\":{},\"id\":\"657aa3fc83cd6b068ef93510\",\"detailParams\":{}},\"contexts\":[]}";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .readValue(body, ChatBotRequest.class);
        Product product = productRepository.findByProductId(Long.parseLong(productId)).get();

        //when
        ChatBotResponse customerProductDetail = productService.getCustomerProductDetail(chatBotRequest);
        String title = customerProductDetail.getTemplate().getOutputs().get(1).getTextCard().getTitle();

        //then
        Assertions.assertThat(title).isEqualTo(product.getName());
    }

    @Test
    void updateProductStatus() throws JsonProcessingException {
        //give
        String utterance =  ProductStatus.판매중.name();

        String body="{\"intent\":{\"id\":\"657aba23eca3c21b078678c3\",\"name\":\"회원 등록상품 상태변경\"},\"userRequest\":{\"params\":{},\"block\":{\"id\":\"657aba23eca3c21b078678c3\",\"name\":\"회원 등록상품 상태변경\"},\"utterance\":\""+utterance+"\",\"lang\":\"ko\",\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":false,\"plusfriendUserKey\":\"QFJSyeIZbO77\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}}},\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"action\":{\"name\":\"회원_등록상품_상태변경\",\"clientExtra\":{\"productId\":\""+productId+"\"},\"params\":{},\"id\":\"657aba4fa678316d2c4af513\",\"detailParams\":{}},\"contexts\":[]}";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .readValue(body, ChatBotRequest.class);

        //when
        ChatBotResponse chatBotResponse = productService.updateProductStatus(chatBotRequest);
        String text = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(text).isEqualTo("상품을 "+utterance+" 상태로 변경하였습니다.");
    }

    @Test
    @Transactional
    void deleteProduct() throws JsonProcessingException {
        //give
        String utterance = ProductStatus.삭제.name();

        String body="{\"intent\":{\"id\":\"657aba23eca3c21b078678c3\",\"name\":\"회원 등록상품 상태변경\"},\"userRequest\":{\"params\":{},\"block\":{\"id\":\"657aba23eca3c21b078678c3\",\"name\":\"회원 등록상품 상태변경\"},\"utterance\":\""+utterance+"\",\"lang\":\"ko\",\"user\":{\"id\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"type\":\"botUserKey\",\"properties\":{\"botUserKey\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"isFriend\":false,\"plusfriendUserKey\":\"QFJSyeIZbO77\",\"bot_user_key\":\"bf1409542da67b26c2262a0a2f72ac6b5df6ad45e49e90543bd4586af560622863\",\"plusfriend_user_key\":\"QFJSyeIZbO77\"}}},\"bot\":{\"id\":\"65262a2d31101d1cb1106082!\",\"name\":\"중계나라\"},\"action\":{\"name\":\"회원_등록상품_상태변경\",\"clientExtra\":{\"productId\":\""+productId+"\"},\"params\":{},\"id\":\"657aba4fa678316d2c4af513\",\"detailParams\":{}},\"contexts\":[]}";
        chatBotRequest = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                .readValue(body, ChatBotRequest.class);

        //when
        ChatBotResponse chatBotResponse = productService.deleteProduct(chatBotRequest);
        String text = chatBotResponse.getTemplate().getOutputs().get(0).getSimpleText().getText();

        //then
        Assertions.assertThat(text).isEqualTo("상품을 정상적으로 삭제하였습니다.");

    }
}