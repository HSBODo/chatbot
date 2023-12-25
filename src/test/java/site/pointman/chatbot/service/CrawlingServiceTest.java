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
        String url = "https://quasarzone.com/bbs/qb_saleinfo";
        String cssQuery = "#frmSearch > div > div.list-board-wrap > div.market-type-list.market-info-type-list.relative > table > tbody > tr";
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select(cssQuery);
        int total = 0;
        for (Element element : elements){
            String status = element.select("span.label").text();
            log.info("status={}",status);
            String s2 = element.select("a.subject-link").text();
            log.info("s2={}",s2);
            String imageUrl = element.select("img.maxImg").get(0).attr("src");
            log.info("imageUrl={}",imageUrl);
            String title = element.select("span.ellipsis-with-reply-cnt").text();
            log.info("title={}",title);
            String price = element.select("span.text-orange").text();
            log.info("price={}",price);
            String category = element.select("span.category").text();
            log.info("category={}",category);
//            String brandImageUrl = element.select("span.brand > img").get(0).attr("src");
//            log.info("category={}",brandImageUrl);
            String detailUrl = "https://quasarzone.com/"+element.select("a.subject-link").get(0).attr("href");
            log.info("detail={}",detailUrl);
//            Document productInfoDetail = Jsoup.connect(detailUrl).get();
//            String purchaseUrl = productInfoDetail.select("#content > div.sub-content-wrap > div.left-con-wrap > div.common-view-wrap.market-info-view-wrap > div > dl > dd > table > tbody > tr:nth-child(1) > td > a").text();
//            String brandName = productInfoDetail.select("#content > div.sub-content-wrap > div.left-con-wrap > div.common-view-wrap.market-info-view-wrap > div > dl > dd > table > tbody > tr:nth-child(2) > td").text();
//
//            SpecialProduct specialProduct = SpecialProduct.builder()
//                    .productThumbnailImageUrl(imageUrl)
//                    .brandName(brandName)
//                    .brandImageUrl(brandImageUrl)
//                    .title(title)
//                    .price(price)
//                    .category(category)
//                    .detailInfoUrl(detailUrl)
//                    .purchaseUrl(purchaseUrl)
//                    .build();
//
//            total++;
//            if(total == 10) break;
        }
    }
}