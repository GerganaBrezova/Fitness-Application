package app.inbox_message.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboxMessageRequest {

    private UUID userId;

    private String header;

    private String content;

    private LocalDateTime sentOn;
}
