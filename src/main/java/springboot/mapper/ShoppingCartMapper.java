package springboot.mapper;

import org.mapstruct.Mapper;
import springboot.config.MapperConfig;
import springboot.dto.shoppingcart.ShoppingCartDto;
import springboot.model.ShoppingCart;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(ShoppingCartDto requestDto);
}

