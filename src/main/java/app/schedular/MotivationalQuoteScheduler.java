package app.schedular;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Getter
@Component
public class MotivationalQuoteScheduler {

    private static final List<String> QUOTES = List.of(
            "Eat well, stay hydrated, and keep moving to stay strong and healthy. 😊",
            "Your body achieves what your mind believes. Stay consistent!",
            "Every workout brings you one step closer to your goal. Keep pushing! 💪",
            "Hydrate, fuel up, and give your best today!",
            "Stronger every day. Progress over perfection!",
            "A one-hour workout is just 4% of your day—make it count!",
            "Small steps every day lead to big results!",
            "The only bad workout is the one you didn’t do!",
            "Your future self will thank you for today's effort!",
            "Discipline beats motivation. Build the habit and results will follow!"
    );

    private String dailyQuote;

    @Scheduled(cron = "0 0 0 * * ?")
    @PostConstruct
    public void updateDailyFitnessQuote() {

        Random random = new Random();
        dailyQuote = QUOTES.get(random.nextInt(QUOTES.size()));
    }
}
