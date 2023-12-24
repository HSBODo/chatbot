package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.service.CrawlingService;

@Slf4j
@Service
public class CrawlingServiceImpl implements CrawlingService {

    @Override
    public Elements getJsoupElements(String url, String cssQuery) throws Exception{
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select(cssQuery);
        return elements;
    }
}
