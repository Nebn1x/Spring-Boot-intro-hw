package springboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.dto.user.UserRegistrationRequestDto;
import springboot.dto.user.UserResponseDto;
import springboot.exeptions.RegistrationException;
import springboot.mapper.UserMapper;
import springboot.model.ShoppingCart;
import springboot.model.User;
import springboot.repository.ShoppingCartRepository;
import springboot.repository.UserRepository;
import springboot.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user with the email "
                    + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
