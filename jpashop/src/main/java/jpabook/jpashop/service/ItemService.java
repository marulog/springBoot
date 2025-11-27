package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = false)
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

    // 준영속 상태의 객체를 변경감지로 업데이트 쿼리 날리기
    // 업데이트 한 필드만 교체됨!! 개꿀
    // merge의 경우 싹다 변경됨
    @Transactional
    public void updateItem(Long itemId, int price, String name, int stockQuantity){
       // 영속 상태에 있는 인스턴스를 찾아옴
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        // 영속 상태의 객체의 필드가 변경된 이후 트랜잭션 종료
        // 종료 후 commit -> flush()를 통해 영속 상태의 객체 중 변경된 걸 탐지
        // 탐지 후 해당 객체들을 DB로 update 쿼리 날림
    }
}
