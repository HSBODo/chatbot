package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.dto.product.ProductDto;
import site.pointman.chatbot.dto.product.SpecialProduct;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CrawlingServiceTest {
    @Autowired
    CrawlingService crawlingService;

    @Test
    void getJsoupElements() throws Exception {
        String price = "349";
        String[] split = price.split("\\.");
        log.info("{}", split[0]);

    }
}