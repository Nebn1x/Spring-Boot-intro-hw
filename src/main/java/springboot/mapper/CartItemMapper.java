package springboot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import springboot.config.MapperConfig;
import springboot.dto.cartitem.AddCartItemRequestDto;
import springboot.dto.cartitem.CartItemDto;
import springboot.dto.cartitem.UpdateCartItemRequestDto;
import springboot.model.CartItem;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface CartItemMapper {
    CartItemDto toDto(CartItem cartItem);

    CartItem toModel(AddCartItemRequestDto requestDto);

    void updateCartItemFromDto(UpdateCartItemRequestDto requestDto,
                               @MappingTarget CartItem item);
}
