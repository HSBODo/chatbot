package site.pointman.chatbot.utill;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class StringUtilsTest {



    @Test
    void replaceAll() {
    }

    @Test
    void formatMoney() {
    }

    @Test
    void formatApproveDate() {
    }

    @Test
    void formatProductDetail() {
    }

    @Test
    void testFormatProductDetail() {
    }

    @Test
    void formatProductInfo() {
    }

    @Test
    void createImgFileName() {
    }

    @Test
    void dateFormat() throws ParseException {
        String date = "2023-12-15 11:36:29";
        date = StringUtils.dateFormat(date, "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");

        log.info("s={}",date);
    }
}