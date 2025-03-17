package springboot.mapper;

import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import springboot.config.MapperConfig;
import springboot.dto.orderitem.OrderItemDto;
import springboot.model.CartItem;
import springboot.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    OrderItem toModel(OrderItemDto orderItemDto);

    @Mappings({
            @Mapping(source = "cartItem.book", target = "book"),
            @Mapping(source = "cartItem.quantity", target = "quantity"),
            @Mapping(expression = "java(cartItem.getBook().getPrice())", target = "price")
    })
    Set<OrderItem> toOrderItems(Set<CartItem> cartItems);
}
