package site.pointman.chatbot.repository.impl;

import site.pointman.chatbot.domain.address.Address;
import site.pointman.chatbot.domain.block.Block;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.AddressRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class AddressRepositoryImpl  implements AddressRepository  {
    private final EntityManager em;

    public AddressRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Address save(Address address) {
        em.persist(address);
        return address;
    }

    @Override
    public Optional<Address> findByAddress(String kakaoUserkey) {
        Address findAddress = em.find(Address.class, kakaoUserkey);

        return Optional.ofNullable(findAddress);
    }

    @Override
    public Optional<Address> findByAddress(Long addressId) {
        Address findAddress = em.find(Address.class, addressId);
        return Optional.ofNullable(findAddress);
    }

    @Override
    public List<Address> findByMemberAddressAll(String kakaoUserkey) {
        return em.createQuery("select a from Address a where a.isUse=:isUse order by a.createDate desc", Address.class)
                .setParameter("isUse","Y")
                .getResultList();
    }

    @Override
    public Optional<Address> update(Address updateAddress) {
        Address findAddress = em.find(Address.class, updateAddress.getKakaoUserkey());
        findAddress.changeName(updateAddress.getName());
        findAddress.changePhone(updateAddress.getPhone());
        findAddress.changePostCode(updateAddress.getPostCode());
        findAddress.changeDetailAddress(updateAddress.getDetailAddress());
        findAddress.changeJibunAddress(updateAddress.getJibunAddress());
        findAddress.changeRoadAddress(updateAddress.getRoadAddress());
        findAddress.changeExtraAddress(updateAddress.getExtraAddress());
        return Optional.ofNullable(findAddress);
    }
}
