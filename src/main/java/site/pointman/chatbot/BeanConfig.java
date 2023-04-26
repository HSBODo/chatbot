package site.pointman.chatbot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.repository.ItemRepository;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.impl.ItemRepositoryImpl;
import site.pointman.chatbot.repository.impl.KakaoMemberRepositoryImpl;
import site.pointman.chatbot.repository.impl.MemberRepositoryImpl;
import site.pointman.chatbot.repository.impl.OrderRepositoryImpl;

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
        return  new KakaoMemberRepositoryImpl(em);
    }
    @Bean
    public ItemRepository kaKaoItemRepository (){
        return  new ItemRepositoryImpl(em);
    }
    @Bean
    public MemberRepository memberRepository(){return new MemberRepositoryImpl(em);
    }
    @Bean
    public OrderRepository orderRepository(){
        return new OrderRepositoryImpl(em);
    }


}
