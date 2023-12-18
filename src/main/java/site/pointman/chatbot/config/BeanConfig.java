package site.pointman.chatbot.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import site.pointman.chatbot.repository.*;
import site.pointman.chatbot.repository.impl.*;


import javax.persistence.EntityManager;

@Configuration
public class BeanConfig {
//    private DataSource dataSource;
    private EntityManager em;

    public BeanConfig(EntityManager em) {
        this.em = em;
    }

//    @Bean
//    public OrderRepository orderRepository(){
//        return new OrderRepositoryImpl(em);
//    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemberRepositoryImpl(em) ;
    }
    @Bean
    public ProductRepository productRepository(){
        return new ProductRepositoryImpl(em);
    }
    @Bean
    public LogRepository logRepository(){
        return new LogRepositoryImpl(em);
    }

}
