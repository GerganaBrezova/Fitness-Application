package app.inbox_message;

import app.inbox_message.client.dto.InboxMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "inbox-message-service", url = "http://localhost:8081")
public interface InboxMessageClient {

    @GetMapping("/api/v1/inbox/{userId}")
    List<InboxMessageRequest> getInboxMessages(@PathVariable UUID userId);

//    @PostMapping("/api/v1/inbox")
//    void sendInboxMessage(@RequestBody InboxMessageRequest inboxMessageRequest);
}
