package springboot.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.dto.user.UserRegistrationRequestDto;
import springboot.dto.user.UserResponseDto;
import springboot.exeptions.RegistrationException;
import springboot.mapper.UserMapper;
import springboot.model.User;
import springboot.repository.UserRepository;
import springboot.service.ShoppingCartService;
import springboot.service.UserService;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ShoppingCartService shoppingCartService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user with the email "
                    + requestDto.getEmail());
        }
        User user = userRepository.save(userMapper.toModel(requestDto));
        shoppingCartService.createShoppingCart(user);

        return userMapper.toUserResponse(user);
    }
}
