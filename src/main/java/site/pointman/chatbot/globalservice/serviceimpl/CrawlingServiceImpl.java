package site.pointman.chatbot.globalservice.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.product.SpecialProduct;
import site.pointman.chatbot.globalservice.CrawlingService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CrawlingServiceImpl implements CrawlingService {

    @Override
    public Elements getJsoupElements(String url, String cssQuery) throws Exception {
        Document document = Jsoup.connect(url).get();
        log.info("Connect");
        Elements elements = document.select(cssQuery);
        return elements;
    }

    @Override
    public List<Element> filterElements(Elements jsoupElements) {
        List<Element> filterElements = new ArrayList<>();

        for (Element element : jsoupElements){
            String status = element.select("span.label").text();
            String title = element.select("a.subject-link").text();
            if (status.equals("종료")) continue;
            if (title.contains("블라인드 처리된 글입니다.")) continue;
            filterElements.add(element);
        }

        return filterElements;
    }

    @Override
    public List<SpecialProduct> getSpecialProducts(List<Element> filterElements, int firstProduct, int lastProduct) throws Exception {
        List<SpecialProduct> specialProducts = new ArrayList<>();

        if (lastProduct > filterElements.size()) lastProduct = filterElements.size();

        for (int i = firstProduct ; i < lastProduct ; i++){
            String imageUrl = "";
            String brandImageUrl = "";
            String detailUrl = "";

            String title = filterElements.get(i).select("span.ellipsis-with-reply-cnt").text();
            String price = filterElements.get(i).select("span.text-orange").text();
            String category = filterElements.get(i).select("span.category").text();
            String status = filterElements.get(i).select("span.label").text();
            if(!filterElements.get(i).select("img.maxImg").isEmpty()) {
                imageUrl = filterElements.get(i).select("img.maxImg").get(0).attr("src");
            }
            if(!filterElements.get(i).select("span.brand > img").isEmpty()){
                brandImageUrl = filterElements.get(i).select("span.brand > img").get(0).attr("src");
            }
            if (!filterElements.get(i).select("a.subject-link").isEmpty()) {
                detailUrl = "https://quasarzone.com/"+filterElements.get(i).select("a.subject-link").get(0).attr("href");
            }

            Thread.sleep(100);
            Document productInfoDetail = Jsoup.connect(detailUrl).get();
            log.info("Detail Connect");

            String purchaseUrl = productInfoDetail.select("table.market-info-view-table > tbody > tr:nth-child(1) > td > a").text();
            String brandName = productInfoDetail.select("table.market-info-view-table > tbody > tr:nth-child(2) > td").text();

            SpecialProduct specialProduct = SpecialProduct.builder()
                    .productThumbnailImageUrl(imageUrl)
                    .brandName(brandName)
                    .brandImageUrl(brandImageUrl)
                    .title(title)
                    .price(price)
                    .category(category)
                    .detailInfoUrl(detailUrl)
                    .purchaseUrl(purchaseUrl)
                    .status(status)
                    .build();
            specialProducts.add(specialProduct);
        }

        return specialProducts;
    }
}
