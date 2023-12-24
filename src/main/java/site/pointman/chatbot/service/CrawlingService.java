package site.pointman.chatbot.service;

import org.jsoup.Connection;
import org.jsoup.select.Elements;

public interface CrawlingService {
    Elements getJsoupElements(String url, String cssQuery) throws Exception;
}
