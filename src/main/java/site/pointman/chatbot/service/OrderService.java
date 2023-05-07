package site.pointman.chatbot.service;

import org.json.simple.JSONObject;

public interface OrderService {

     int calculateTotalPrice (Long itemId,Long optionId, int quantity);
}
