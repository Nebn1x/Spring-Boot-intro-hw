package springboot.service;

import springboot.dto.cartitem.AddCartItemRequestDto;
import springboot.dto.cartitem.CartItemDto;
import springboot.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getCartById(Long userId);

    AddCartItemRequestDto addToCart(CartItemDto requestDto);

    AddCartItemRequestDto updateCartItem(Long cartItemId, CartItemDto requestDto);

    void deleteCartItem(Long cartItemId);
}
