package site.pointman.chatbot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.domain.kakaochatbotuiresponse.*;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.repository.KakaoUserLocationRepository;
import site.pointman.chatbot.repository.impl.JpaKakaoMemberLocationRepository;
import site.pointman.chatbot.repository.impl.JpaKakaoMemberRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class BeanConfig {
//    private DataSource dataSource;
    private EntityManager em;
//    private KakaoMemberRepository kakaoMemberRepository;


    public BeanConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public BasicCard basicCard(){
        return new BasicCard();
    }
    @Bean
    public SimpleText simpleText(){
        return new SimpleText();
    }
    @Bean
    public SimpleImage simpleImage(){
        return new SimpleImage();
    }
    @Bean
    public Buttons buttons(){
        return new Buttons();
    }
    @Bean
    public KakaoMemberRepository kakaoMemberRepository(){
        return  new JpaKakaoMemberRepository(em);
    }
    @Bean
    public KakaoUserLocationRepository kakaoUserLocationRepository(){
        return  new JpaKakaoMemberLocationRepository(em);
    }

}
