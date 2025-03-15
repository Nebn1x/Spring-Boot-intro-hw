package springboot.service;

import java.util.List;
import springboot.dto.orderitem.OrderItemDto;

public interface OrderItemService {

    List<OrderItemDto> getItemsByOrderId(Long orderId);

    OrderItemDto getItemByIdFromOrderId(Long orderId, Long itemId);
}
