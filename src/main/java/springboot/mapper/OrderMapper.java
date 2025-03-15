package springboot.mapper;

import org.mapstruct.Mapper;
import springboot.config.MapperConfig;
import springboot.dto.order.CreateOrderRequestDto;
import springboot.dto.order.OrderDto;
import springboot.model.CartItem;
import springboot.model.Order;
import springboot.model.OrderItem;

import java.util.Set;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderDto toDto(Order order);

    Order toModel(CreateOrderRequestDto requestDto);

    Set<OrderItem> toOrderItems(Set<CartItem> cartItems);
}
