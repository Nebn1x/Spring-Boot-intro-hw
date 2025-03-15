package springboot.service.impl;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springboot.dto.order.CreateOrderRequestDto;
import springboot.dto.order.OrderDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.mapper.OrderMapper;
import springboot.model.Order;
import springboot.model.OrderItem;
import springboot.model.ShoppingCart;
import springboot.repository.OrderItemRepository;
import springboot.repository.OrderRepository;
import springboot.repository.ShoppingCartRepository;
import springboot.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart cart = shoppingCartRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user: " + userId));
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Shopping cart is empty");
        }
        Order order = orderMapper.toModel(requestDto);
        Set<OrderItem> orderItems = orderMapper.toOrderItems(cart.getCartItems());

        order.setOrderItems(orderItems);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, Order.Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found for id: " + orderId));
        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }
}
