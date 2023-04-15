package site.pointman.chatbot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.domain.kakaochatbotui.*;
import site.pointman.chatbot.repository.KaKaoItemRepository;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.repository.impl.JpaKaKaoItemRepositoryImpl;
import site.pointman.chatbot.repository.impl.JpaKakaoMemberRepositoryImpl;

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
    public CommerceCard commerceCard(){
        return new CommerceCard();
    }
    @Bean
    public KakaoMemberRepository kakaoMemberRepository(){
        return  new JpaKakaoMemberRepositoryImpl(em);
    }
    @Bean
    public KaKaoItemRepository kaKaoItemRepository (){
        return  new JpaKaKaoItemRepositoryImpl(em);
    }

}
