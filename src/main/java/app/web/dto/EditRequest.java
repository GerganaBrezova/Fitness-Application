package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditRequest {

    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 symbols.")
    private String username;

    @Size(max = 30, message = "Name can not be longer than 30 characters.")
    private String name;

    @Email(message = "Please enter correct email format.")
    @NotBlank(message = "Email is required.")
    private String email;

    @URL(message = "Please enter correct link format.")
    private String profilePictureUrl;
}
