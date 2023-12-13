package site.pointman.chatbot.utill;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import site.pointman.chatbot.dto.customer.CustomerDto;
import site.pointman.chatbot.dto.product.ProductDto;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class StringUtilsTest {

    @Test
    void createImgFileName() {
        CustomerDto customerDto = CustomerDto.builder()
                .userKey("test")
                .build();
        ProductDto productDto = ProductDto.builder()
                .customer(customerDto.toEntity())
                .productName("상품명")
                .build();
        String imgFileName = StringUtils.createImgFileName(productDto);

        log.info("fileName={}",imgFileName);
    }
}