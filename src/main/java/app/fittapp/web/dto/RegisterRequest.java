package app.fittapp.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 symbols.")
    private String username;

    @Email(message = "Please enter a valid email format.")
    private String email;

    @Size(min = 5, message = "Password must be at least 5 symbols.")
    private String password;

    @Size(min = 5, message = "Password must be at least 5 symbols.")
    private String confirmPassword;

}
