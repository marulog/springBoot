package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.dto.OrderDto;
import jpabook.jpashop.dto.OrderFlatDto;
import jpabook.jpashop.dto.OrderQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("api/v1/orders")
    public List<Order> orderV1(){
        List<Order> all = orderRepository.finaAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
           orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    // dto로 변환은 했는데, sql 너무 많이 나가고 있음 -> 컬랙션이라 그럼
    @GetMapping("api/v2/orders")
    public List<OrderDto> orderV2(){
        List<Order> orders = orderRepository.finaAll(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    // DB입장에서 컬랙션 1:N에 대한 조인은 N개로 데이터가 늘어나게됨
    // 1개의 주문에는 2개의 주문 상품이 있음 order, oderItem fetch join시 2개 행 나옴
    // distinct 키워드를 쿼리에 넣어서 앤티티 중복 제거
    // 다만 이 API는 페이징이 메모리에서 진행됨 -> 오버플로우남
    @GetMapping("api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order> orders = orderRepository.findAllWithItem();

        for (Order order : orders) {
            System.out.println("order ref = " + order + "order = " + order.getId());
        }


        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }


    @GetMapping("api/v3.1/orders")
    public List<OrderDto> orderV3_page(
            @RequestParam(value= "offset", defaultValue = "0") int offset,
            @RequestParam(value= "limit", defaultValue = "100") int limit){
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

//    @GetMapping("api/v6/orders")
//    public List<OrderQueryDto> ordersV6(){
//        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
//        // 노가다 하면 되는데 이해 못해서 pass함 ㅅㄱ ㅋㅋ
//    }
}
