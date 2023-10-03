package site.pointman.chatbot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import site.pointman.chatbot.repository.*;
import site.pointman.chatbot.repository.impl.*;


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
    public OrderRepository orderRepository(){
        return new OrderRepositoryImpl(em);
    }


}
