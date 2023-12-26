package site.pointman.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.pointman.chatbot.repository.*;
import site.pointman.chatbot.repository.custom.*;
import site.pointman.chatbot.repository.custom.impl.*;
import site.pointman.chatbot.repository.impl.*;

import javax.persistence.EntityManager;

@Configuration
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
    public LogRepository logRepository(){
        return new LogRepositoryImpl(em);
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
