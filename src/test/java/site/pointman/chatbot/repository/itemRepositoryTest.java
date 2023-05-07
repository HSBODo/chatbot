package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.util.CollectionUtils;
import site.pointman.chatbot.domain.item.Item;
import site.pointman.chatbot.domain.item.ItemOption;
import site.pointman.chatbot.domain.item.ItemOptionCategory;
import site.pointman.chatbot.domain.member.MemberAttribute;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.order.OrderStatus;
import site.pointman.chatbot.domain.order.PayMethod;
import site.pointman.chatbot.dto.ItemOptionDto;
import site.pointman.chatbot.dto.MemberAttributeDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
class itemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void save() {
        Item item = Item.builder()
                .itemCode(7L)
                .profileNickname("테스트상품")
                .profileImgUrl("www.")
                .description("테스트상품설명")
                .thumbnailImgUrl("상품이미지 url")
                .thumbnailLink("상품이미지 링크")
                .price(10000)
                .discountedPrice(5000)
                .discount(5000)
                .discountRate(0)
                .currency("")
                .is_display("")
                .total_quantity(0L)
                .build();
        itemRepository.save(item);
    }

    @Test
    void findByDisplayItems() {
        List<Item> items = itemRepository.findByDisplayItems();
        if(items.isEmpty()) throw new NullPointerException("display 가능한 아이템이 없습니다.");
        items.stream()
                .forEach(item -> {
                    log.info("isDisplay={}",item.getIs_display());
                    Assertions.assertThat(item.getIs_display()).isEqualTo("Y");
                });
    }

    @Test
    void findByItem(){
        Long itemCode = 1L;
        Optional<Item> maybeItem = itemRepository.findByItem(itemCode);
        if (maybeItem.isEmpty()) throw new NullPointerException("아이템 코드와 일치하는 상품 없음");
        Item item = maybeItem.get();
        Assertions.assertThat(item.getItemCode()).isEqualTo(itemCode);

    }
    @Test
    @Commit
    void saveItemOption(){
        ItemOptionDto itemOptionDto = ItemOptionDto.builder()
                .itemCode(3L)
                .optionName("260")
                .optionPrice(0)
                .category(ItemOptionCategory.사이즈)
                .build();

        ItemOption itemOption = itemOptionDto.toEntity();
        itemRepository.saveItemOption(itemOption);
    }

    @Test
    void findByItemOptions(){

        List<ItemOption> maybeItemOptions = itemRepository.findByItemOptions(3L,ItemOptionCategory.색상);
        log.info("null",CollectionUtils.isEmpty(maybeItemOptions));
        if(CollectionUtils.isEmpty(maybeItemOptions)) throw new NullPointerException("옵션이 없습니다");
        maybeItemOptions.stream().forEach(itemOption -> {
            log.info("optName={}",itemOption.getOptionName());
        });

    }













}