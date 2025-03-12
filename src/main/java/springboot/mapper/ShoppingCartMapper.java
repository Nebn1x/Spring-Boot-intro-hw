package springboot.mapper;

import org.mapstruct.Mapper;
import springboot.config.MapperConfig;
import springboot.dto.shoppingcart.ShoppingCartDto;
import springboot.model.ShoppingCart;

@Mapper(componentModel = "spring",
        uses = CartItemMapper.class,config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
