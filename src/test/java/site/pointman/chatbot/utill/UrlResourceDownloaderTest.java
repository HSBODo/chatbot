package site.pointman.chatbot.utill;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UrlResourceDownloaderTest {

    @Test
    void download() {
//        String url = "http://secure.kakaocdn.net/dna/bAEjvb/K6befWy2VV/XXX/img_org.jpg?credential=Kq0eSbCrZgKIq51j                                         h41Uf1jLsUh7VWcz&expires=1702441131&allow_ip=&allow_referer=&signature=34ahBectBjRTMHq7G1mTtyNMT4M%3D";
//        UrlResourceDownloader urlResourceDownloader = new UrlResourceDownloader("src/main/resources/image/"+ UUID.randomUUID().toString()+".jpg");
//        urlResourceDownloader.download(url);
        File file = new File("src/main/resources/image/0e96659b-4c5c-4a21-83e8-d7cb9b1103d1.jpg");
        System.out.println("file = " + file.getName());
    }
}