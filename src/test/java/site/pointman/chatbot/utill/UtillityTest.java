package site.pointman.chatbot.utill;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class UtillityTest {
    Utillity utillity;

    @Test
    void replaceAll() {
    }

    @Test
    void formatMoney() {
    }

    @Test
    void formatApproveDate()throws Exception {

        String date ="2023-04-17T22:04:24";
        String formatApproveDate = utillity.formatApproveDate(date);
        log.info(formatApproveDate);
    }
}