package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();

        Book book = createBook("준희 짱짱맨", 12000, 10);

        //when
        int orderCount = 2;
        // 상품 주문 생성 -> 그걸 바탕으로 오더 생성 -> 오더 번호 반환
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        //then
        Order findOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.ORDER, findOrder.getStatus(), "상품 주문시 상태는 ORDER");
        Assertions.assertEquals(1, findOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다");
        Assertions.assertEquals(12000 * orderCount, findOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        Assertions.assertEquals(8, book.getStockQuantity() );
    }


    @Test()
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Item item = createBook("준희 짱짱맨", 12000, 10);

        int orderCount = 2;

        //then
        Assertions.assertThrows(NotEnoughStockException.class,
                ()->  orderService.order(member.getId(), item.getId(), orderCount));
    }


    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Book item = createBook("준희 짱짱맨", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);


        //when
        orderService.cancelOrder(orderId);

        //then
        Order findOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(10, item.getStockQuantity(), "주문 취소 시 재고 수량도 취소되어야 합니다.");
        Assertions.assertEquals(OrderStatus.CANCEL, findOrder.getStatus(), "주문 취소 시 상태는 CANCEL이다.");


    }

    @Test
    public void 상품재고수량초과() throws Exception{
        //given

        //when

        //then
    }



    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book(); // Item
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member(); // Member
        member.setName("회원1");
        member.setAddress(new Address("서울", "감기", "123-122"));
        em.persist(member);
        return member;
    }
}