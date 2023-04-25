package site.pointman.chatbot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.repository.ItemRepository;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.impl.ItemRepositoryImpl;
import site.pointman.chatbot.repository.impl.JpaKakaoMemberRepositoryImpl;
import site.pointman.chatbot.repository.impl.JpaMemberRepositoryImpl;

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
    public KakaoMemberRepository kakaoMemberRepository(){
        return  new JpaKakaoMemberRepositoryImpl(em);
    }
    @Bean
    public ItemRepository kaKaoItemRepository (){
        return  new ItemRepositoryImpl(em);
    }
    @Bean
    public MemberRepository memberRepository(){return new JpaMemberRepositoryImpl(em);
    }


}
