package springboot.service.impl;

import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.dto.order.CreateOrderRequestDto;
import springboot.dto.order.OrderDto;
import springboot.dto.order.OrderStatusRequestDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.exeptions.OrderProcessingException;
import springboot.mapper.OrderItemMapper;
import springboot.mapper.OrderMapper;
import springboot.model.Order;
import springboot.model.OrderItem;
import springboot.model.ShoppingCart;
import springboot.repository.OrderRepository;
import springboot.repository.ShoppingCartRepository;
import springboot.service.OrderService;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart cart = shoppingCartRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user: " + userId));
        if (cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Shopping cart is empty");
        }
        Order order = orderMapper.toModel(requestDto);
        Set<OrderItem> orderItems = orderItemMapper.toOrderItems(cart.getCartItems());

        order.setOrderItems(orderItems);
        order.setTotal(calculateTotalPrice(orderItems));

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable, Long userId) {
        return orderRepository.findByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found for id: " + orderId));
        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    private BigDecimal calculateTotalPrice(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
