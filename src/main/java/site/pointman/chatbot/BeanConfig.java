package site.pointman.chatbot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.dto.repository.*;
import site.pointman.chatbot.dto.repository.impl.*;


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
    public ItemRepository kaKaoItemRepository (){
        return  new ItemRepositoryImpl(em);
    }
    @Bean
    public MemberRepository memberRepository(){return new MemberRepositoryImpl(em);  }
    @Bean
    public OrderRepository orderRepository(){
        return new OrderRepositoryImpl(em);
    }
    @Bean
    public BlockRepository blockRepository() {return new BlockRepositoryImpl(em);}
    @Bean
    public AddressRepository addressRepository(){
        return  new AddressRepositoryImpl(em);
    }


}
