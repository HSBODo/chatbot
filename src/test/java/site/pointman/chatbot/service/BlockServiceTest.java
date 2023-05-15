package site.pointman.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
class BlockServiceTest {

    @Autowired
    BlockService blockService;

    @Test
    void createBlock() {
    }

    @Test
    void chatBotController() {
    }

    @Test
    void createJoinBlock() {
    }

    @Test
    void createMemberInfoBlock() {
    }

    @Test
    void createItemOptionBlock() {
    }

    @Test
    void createItemQuantityBlock() {
    }

    @Test
    void createAddAddressBlock() throws Exception {
        String userkey="QFERwysZbO77";
        JSONObject addAddressBlock = blockService.createAddAddressBlock(userkey);
        log.info("address={}",addAddressBlock);
    }

    @Test
    void createEstimateBlock() {
    }
}