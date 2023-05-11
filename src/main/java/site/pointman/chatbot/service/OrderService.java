package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.domain.item.Item;

import java.util.List;

public interface OrderService {

     int calculateTotalPrice (Long itemId,Long optionId, int quantity);

     List<Item> findBySalesRank ();
}
