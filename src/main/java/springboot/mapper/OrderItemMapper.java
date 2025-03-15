package springboot.mapper;

import org.mapstruct.Mapper;
import springboot.config.MapperConfig;
import springboot.dto.orderitem.OrderItemDto;
import springboot.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    OrderItem toModel(OrderItemDto orderItemDto);
}
