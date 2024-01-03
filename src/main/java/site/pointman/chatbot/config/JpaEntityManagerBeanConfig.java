package site.pointman.chatbot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.repository.customrepository.*;
import site.pointman.chatbot.repository.customrepository.impl.*;

import javax.persistence.EntityManager;

@Configuration
@Slf4j
public class JpaEntityManagerBeanConfig {
//    private DataSource dataSource;
    private EntityManager em;

    public JpaEntityManagerBeanConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberCustomRepository memberCustomRepository(){
        return new MemberCustomRepositoryImpl(em) ;
    }
    @Bean
    public ProductCustomRepository productCustomRepository(){
        return new ProductCustomRepositoryImpl(em);
    }
    @Bean
    public NoticeCustomRepository noticeCustomRepository(){
        return new NoticeCustomRepositoryImpl(em);
    }
    @Bean
    public OrderCustomRepository orderCustomRepository(){
        return new OrderCustomRepositoryImpl(em);
    }
    @Bean
    public PaymentCustomRepository paymentCustomRepository(){
        return new PaymentCustomRepositoryImpl(em);
    }
}
