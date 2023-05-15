package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.domain.block.BlockServiceType;
import site.pointman.chatbot.dto.block.BlockDto;

public interface BlockService {
    JSONObject createBlock(BlockDto blockDto);
    JSONObject chatBotController(String kakaoUserkey, BlockServiceType blockService, JSONObject buttonParams) throws Exception;

    JSONObject createJoinBlock(BlockDto blockDto, String kakaoUserkey) throws Exception;

    JSONObject createMemberInfoBlock(BlockDto blockDto, String kakaoUserkey) throws Exception;
    JSONObject createItemOptionBlock(BlockDto blockDto, String kakaoUserkey, Long itemCode) throws Exception;

    JSONObject createItemQuantityBlock(BlockDto blockDto) throws Exception;
    JSONObject createAddAddressBlock(String kakaoUserkey) throws Exception;

    JSONObject createEstimateBlock(BlockDto blockDto, String kakaoUserkey) throws Exception;
}
