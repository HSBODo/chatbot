package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.address.Address;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;

import java.math.BigDecimal;
import java.util.Optional;

public interface AddressRepository {

    Address save(Address address);
    Optional<Address> findByAddress(String kakaoUserkey);
    Optional<Address> findByAddress(Long addressId);
    Optional<Address> update (Address updateAddress);
}
