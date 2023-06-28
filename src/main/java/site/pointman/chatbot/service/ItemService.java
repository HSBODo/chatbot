package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.dto.RequestDto;

public interface ItemService {
    JSONObject createNotice(RequestDto reqDto) throws Exception;
    JSONObject createItemName(RequestDto requestDto)throws Exception;
}
