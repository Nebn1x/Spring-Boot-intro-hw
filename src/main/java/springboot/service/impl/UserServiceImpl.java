package springboot.service.impl;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.dto.user.UserRegistrationRequestDto;
import springboot.dto.user.UserResponseDto;
import springboot.exeptions.RegistrationException;
import springboot.mapper.UserMapper;
import springboot.model.Role;
import springboot.model.User;
import springboot.repository.RoleRepository;
import springboot.repository.UserRepository;
import springboot.service.ShoppingCartService;
import springboot.service.UserService;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user with the email "
                    + requestDto.getEmail());
        }
        Role role = roleRepository.byRoleName(Role.RoleName.USER);
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(role));
        userRepository.save(user);
        shoppingCartService.createShoppingCart(user);
        return userMapper.toUserResponse(user);
    }
}
