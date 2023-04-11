package site.pointman.chatbot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.domain.kakaochatbotui.*;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.repository.impl.JpaKakaoMemberRepository;

import javax.persistence.EntityManager;

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

}
