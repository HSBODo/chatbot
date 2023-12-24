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
            String s = element.select("p.tit > span").text();
            String s2 = element.select("p.tit > a").text();
            log.info("s={}",s2);
            if(s.equals("종료"));
            String imageUrl = element.select("td:nth-child(2) > div > div.thumb-wrap > a > img").get(0).attr("src");
            String title = element.select("td:nth-child(2) > div > div.market-info-list-cont > p > a > span").text();
            String price = element.select("td:nth-child(2) > div > div.market-info-list-cont > div > p > span:nth-child(2) > span").text();
//            String category = element.select("td:nth-child(2) > div > div.market-info-list-cont > div.market-info-sub > p > span.category").text();
//            String brandImageUrl = element.select("td:nth-child(2) > div > div.market-info-list-cont > div.market-info-sub > p > span.brand > img").get(0).attr("src");
//            String detailUrl = "https://quasarzone.com/"+element.select("td:nth-child(2) > div > div.thumb-wrap > a").get(0).attr("href");
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