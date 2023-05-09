package site.pointman.chatbot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.pointman.chatbot.domain.address.Address;
import site.pointman.chatbot.domain.address.AddressRepositoryDataJpa;


import java.util.List;
import java.util.Optional;

@SpringBootTest
class AddressRepositoryTest {
    @Autowired
    AddressRepositoryDataJpa addressRepository;

    @Test
    void findByAddress() {
        Long id = 2L;
        Optional<Address> byId = addressRepository.findById(id);

    }
}