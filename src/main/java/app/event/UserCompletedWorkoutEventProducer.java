package app.event;

import app.event.payload.UserCompletedWorkoutEvent;
import app.event.payload.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCompletedWorkoutEventProducer {

    private final KafkaTemplate<String, UserCompletedWorkoutEvent> kafkaTemplate;

    @Autowired
    public UserCompletedWorkoutEventProducer(KafkaTemplate<String, UserCompletedWorkoutEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(UserCompletedWorkoutEvent userCompletedWorkoutEvent) {

        kafkaTemplate.send("user-completed-workout-event.v1", userCompletedWorkoutEvent);

        log.info("Successfully sent user-completed-workout-event for user %s.".formatted(userCompletedWorkoutEvent.getUserId()));
    }
}
