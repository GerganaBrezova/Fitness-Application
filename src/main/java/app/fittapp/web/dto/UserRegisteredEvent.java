package app.fittapp.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserRegisteredEvent {

    private UUID userId;
}
