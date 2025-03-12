package springboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.dto.cartitem.AddCartItemRequestDto;
import springboot.dto.cartitem.UpdateCartItemRequestDto;
import springboot.dto.shoppingcart.ShoppingCartDto;
import springboot.exeptions.EntityNotFoundException;
import springboot.mapper.CartItemMapper;
import springboot.mapper.ShoppingCartMapper;
import springboot.model.CartItem;
import springboot.model.ShoppingCart;
import springboot.model.User;
import springboot.repository.CartItemRepository;
import springboot.repository.ShoppingCartRepository;
import springboot.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto getCartById(Long userId) {
        return shoppingCartRepository.findById(userId)
                .map(shoppingCartMapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user: " + userId));

    }

    @Override
    public ShoppingCartDto addToCart(Long userId, AddCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for user: " + userId));
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        shoppingCart.getCartItems().add(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto updateCartItem(Long cartItemId, UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cart item not found: " + cartItemId));
        ShoppingCart shoppingCart = shoppingCartRepository.findById(
                cartItem.getShoppingCart().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Shopping cart not found for" + cartItemId));

        cartItemMapper.updateCartItemFromDto(requestDto, cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
