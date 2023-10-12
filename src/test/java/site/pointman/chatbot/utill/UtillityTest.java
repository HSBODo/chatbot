package site.pointman.chatbot.utill;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

@Slf4j
class UtillityTest {
    StringUtils utillity;
    @Test
    void replaceAll() {
    }

    @Test
    void formatMoney() {
    }

    @Test
    void base64() throws NoSuchAlgorithmException {
        String lssUser = "ROBKALBATC";
        String lssPass = "Ra3Om4Ba";
        String officeId = "SELLO8KAL";
        String lssOrg = "LOY-KAL";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        byte[] nonce = UUID.randomUUID().toString().replace("-", "").substring(0, 16).getBytes();
        String timeIso = formatter.format(Calendar.getInstance().getTime());
        byte[] timeIsoArr = timeIso.getBytes();
        byte[] sha1pass = MessageDigest.getInstance("SHA-1").digest(lssPass.getBytes());

        //concatenate three arrays
        byte[] concat = new byte[nonce.length + timeIsoArr.length + sha1pass.length];
        System.arraycopy(nonce,0,concat,0, nonce.length);
        System.arraycopy(timeIsoArr,0,concat,nonce.length, timeIsoArr.length);
        System.arraycopy(sha1pass,0, concat, nonce.length + timeIsoArr.length, sha1pass.length);
        byte[] sha1digest = MessageDigest.getInstance("SHA-1").digest(concat);
        String saltedPass = new String(Base64.getEncoder().encode(sha1digest));
        String base64nonce = new String(Base64.getEncoder().encode(nonce));
        String jToken = String.format("{\"userId\": \"%s\"}",
                lssUser);

        log.info("endcode={}","1AAuth "+ Base64.getEncoder().encodeToString(jToken.getBytes()));
    }

    @Test
    void formatApproveDate()throws Exception {

        String date ="2023-04-17T22:04:24";
        String formatApproveDate = utillity.formatApproveDate(date);
        log.info(formatApproveDate);
    }
}