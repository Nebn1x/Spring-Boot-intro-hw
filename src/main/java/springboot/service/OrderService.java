package springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springboot.dto.order.CreateOrderRequestDto;
import springboot.dto.order.OrderDto;
import springboot.model.Order;

public interface OrderService {

    OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto);

    Page<OrderDto> getAllOrders(Pageable pageable, Long userId);

    OrderDto updateOrderStatus(Long orderId, Order.Status status);
}
