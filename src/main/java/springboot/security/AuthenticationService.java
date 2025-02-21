package springboot.security;

import java.security.SecureRandom;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.dto.user.UserLoginRequestDto;
import springboot.model.User;
import springboot.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    public String authenticate(UserLoginRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.email());
        if (user.isEmpty()) {
            throw new RuntimeException("Can't login");
        }
        String userPasswordFromDb = user.get().getPassword();
        String hashedPassword = HashUtil.hashPassword(requestDto.password(), generateSalt());
        if (!hashedPassword.equals(userPasswordFromDb)) {
            throw new RuntimeException("Can't login");
        }
        return tokenUtil.generateToken(requestDto.email());
    }

    private byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }
}
