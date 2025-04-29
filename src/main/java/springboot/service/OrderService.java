package springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import springboot.dto.order.CreateOrderRequestDto;
import springboot.dto.order.OrderDto;
import springboot.dto.order.OrderStatusRequestDto;
import springboot.dto.orderitem.OrderItemDto;

public interface OrderService {

    OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto);

    Page<OrderDto> getAllOrders(Pageable pageable, Long userId);

    OrderDto updateOrderStatus(Long orderId, OrderStatusRequestDto requestDto);

    Page<OrderItemDto> getItemsByOrderId(Pageable pageable, Long orderId);

    OrderItemDto getItemByIdFromOrderId(Long orderId, Long itemId);
}
