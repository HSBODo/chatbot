package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.dto.BlockDto;

public interface BlockService {

    JSONObject createBlock(String kakaoUserkey, BlockDto blockDto) throws Exception;
}
