package site.pointman.chatbot.globalservice;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import site.pointman.chatbot.domain.product.SpecialProduct;

import java.util.List;

public interface CrawlingService {
    Elements getJsoupElements(String url, String cssQuery) throws Exception;
    List<Element> filterElements(Elements jsoupElements);
    List<SpecialProduct> getSpecialProducts(List<Element> filterElements, int firstProduct, int lastProduct) throws Exception;
}
