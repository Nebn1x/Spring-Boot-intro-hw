package springboot.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.dto.orderitem.OrderItemDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.mapper.OrderItemMapper;
import springboot.model.OrderItem;
import springboot.repository.OrderItemRepository;
import springboot.repository.OrderRepository;
import springboot.service.OrderItemService;

@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemDto> getItemsByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getItemByIdFromOrderId(Long orderId, Long itemId) {
        if (orderRepository.existsById(orderId)) {
            OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Item not found in order."
                                    + " Order Id: " + orderId
                                    + ", Item Id: " + itemId));
            return orderItemMapper.toDto(orderItem);
        }
        throw new EntityNotFoundException("Order not found with id: " + orderId);
    }
}
