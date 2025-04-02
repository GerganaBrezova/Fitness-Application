package app.schedular;

import app.meal.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MealHistoryCleanupScheduler {

    private final MealService mealService;

    @Autowired
    public MealHistoryCleanupScheduler(MealService mealService) {
        this.mealService = mealService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupMealHistory() {

        mealService.deleteAllMeals();
    }
}
