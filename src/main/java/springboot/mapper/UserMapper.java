package springboot.mapper;

import org.mapstruct.Mapper;
import springboot.config.MapperConfig;
import springboot.dto.user.UserRegistrationRequestDto;
import springboot.dto.user.UserResponseDto;
import springboot.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toUserResponse(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
