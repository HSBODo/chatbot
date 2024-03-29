package site.pointman.chatbot.globalservice;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.product.dto.ProductImageDto;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class S3FileServiceTest {
    @Autowired
    S3FileService s3FileService;
    private final String S3_REPOSITORY ="https://pointman-file-repository.s3.ap-northeast-2.amazonaws.com/";


    @Test
    void uploadProductImage() {
        //give
        List<String> imageUrls = new ArrayList<>();
        String imageUrl = "https://pointman-file-repository.s3.ap-northeast-2.amazonaws.com/image/%ED%85%8C%EC%8A%A4%ED%8A%B8.jpg";
        String userKey = "테스트유저키";
        String productName = "테스트상품명";
        imageUrls.add(imageUrl);

        //when
        ProductImageDto productImageDto = s3FileService.uploadImages(imageUrls, userKey, productName,"image");
        List<String> returnImageUrl = productImageDto.getImageUrls();

        //then
        Assertions.assertThat(returnImageUrl.get(0)).contains(S3_REPOSITORY);
    }
}