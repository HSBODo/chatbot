package site.pointman.chatbot.service;

import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import site.pointman.chatbot.dto.product.SpecialProduct;

import java.util.List;

public interface CrawlingService {
    Elements getJsoupElements(String url, String cssQuery) throws Exception;

    List<Element> filterElements(Elements jsoupElements);
    List<SpecialProduct> getSpecialProducts(List<Element> filterElements, int firstProduct, int lastProduct) throws Exception;
}
