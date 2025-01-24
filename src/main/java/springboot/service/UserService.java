package springboot.service;

import springboot.dto.user.UserRegistrationRequestDto;
import springboot.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
