package springboot.security;

import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    private static final String SEPARATOR = ":";
    private static final String SECRET = "mySuperSecretString";

    /**
     * Generates a token based on the username.
     *
     * @param username the username to be encoded in the token
     * @return a token representing the username
     */
    public String generateToken(String username) {
        // Concatenate the username, the secret, and the current time to ensure uniqueness
        String rawToken = username + SEPARATOR + System.currentTimeMillis() + SEPARATOR + SECRET;
        // Convert to a hex string
        return toHexString(rawToken.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validates the token by trying to parse it and checking the secret.
     *
     * @param token the token to be validated
     * @return true if the token is structurally
     * valid and contains the correct secret, false otherwise
     */
    public boolean isValidToken(String token) {
        try {
            String rawToken = new String(fromHexString(token), StandardCharsets.UTF_8);
            String[] parts = rawToken.split(SEPARATOR);
            if (parts.length == 3) {
                // Check if the secret in the token matches the secret we have
                return SECRET.equals(parts[2]);
            }
            // add validation about token expiration
        } catch (IllegalArgumentException e) {
            // The token structure is not valid
        }
        return false;
    }

    /**
     * Extracts the username from the token.
     *
     * @param token the token from which the username is to be extracted
     * @return the username if the token is valid, null otherwise
     */
    public String extractUsername(String token) {
        if (!isValidToken(token)) {
            return null;
        }
        String rawToken = new String(fromHexString(token), StandardCharsets.UTF_8);
        return rawToken.split(SEPARATOR)[0];
    }

    private String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private byte[] fromHexString(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) {
        // Example usage
        TokenUtil tokenUtil = new TokenUtil();

        String username = "user123";
        String token = tokenUtil.generateToken(username);
        System.out.println("Generated Token: " + token);

        boolean isValid = tokenUtil.isValidToken(token);
        System.out.println("Is token valid? " + isValid);

        String extractedUsername = tokenUtil.extractUsername(token);
        System.out.println("Extracted Username: " + extractedUsername);
    }
}
