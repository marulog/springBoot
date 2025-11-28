package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.SimpleOrderDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * X To One 관계(ManToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // Entity를 바로 가져다 쓰지 말고 DTO롤 사용하자 ㅇㅇ
    @GetMapping("api/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.finaAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // 강제 초기화
        }

        return all;
    }

    // 아~ 쉽지않음 ㅋㅋ
    @GetMapping("api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2(){
        List<Order> orders = orderRepository.finaAll(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    // fetch join 사용
    @GetMapping("api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }


    // 내가 원하는 필드만 가져올 수 있음 -> 직접 select문으로 필드명 명시했음
    // 단 재사용성이 떨어짐, 요즘은 V3랑 큰 성능 차이가 없다고 함
    @GetMapping("api/v4/simple-orders")
    public List<SimpleOrderDto> ordersV4(){
        return orderRepository.findOrderDtos();
    }

}
