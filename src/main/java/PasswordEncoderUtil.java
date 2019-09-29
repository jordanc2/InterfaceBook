import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
 public static void main(String[] args) {
     BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
     String encoded = encoder.encode(args[0]);
     System.out.println(encoded);
 }
}