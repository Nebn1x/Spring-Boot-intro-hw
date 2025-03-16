package springboot.mapper;

import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import springboot.config.MapperConfig;
import springboot.dto.order.CreateOrderRequestDto;
import springboot.dto.order.OrderDto;
import springboot.model.CartItem;
import springboot.model.Order;
import springboot.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderDto toDto(Order order);

    Order toModel(CreateOrderRequestDto requestDto);

    @Mappings({
            @Mapping(source = "cartItem.book", target = "book"),
            @Mapping(source = "cartItem.quantity", target = "quantity"),
            @Mapping(expression = "java(cartItem.getBook().getPrice())", target = "price")
    })
    Set<OrderItem> toOrderItems(Set<CartItem> cartItems);
}
