package springboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.dto.cartitem.AddCartItemRequestDto;
import springboot.dto.cartitem.CartItemDto;
import springboot.dto.shoppingcart.ShoppingCartDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.mapper.CartItemMapper;
import springboot.mapper.ShoppingCartMapper;
import springboot.model.CartItem;
import springboot.repository.CartItemRepository;
import springboot.repository.ShoppingCartRepository;
import springboot.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    public ShoppingCartDto getCartById(Long userId) {
        return shoppingCartRepository.findById(userId)
                .map(shoppingCartMapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user: " + userId));

    }

    public AddCartItemRequestDto addToCart(CartItemDto requestDto) {
        CartItem item = cartItemMapper.toModel(requestDto);
        return cartItemMapper.toDto(cartItemRepository.save(item));
    }

    public AddCartItemRequestDto updateCartItem(Long cartItemId, CartItemDto requestDto) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cart item not found: " + cartItemId));
        cartItemMapper.updateCartItemFromDto(requestDto, item);
        return cartItemMapper.toDto(cartItemRepository.save(item));
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
