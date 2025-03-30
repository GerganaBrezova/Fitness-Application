package app.fittapp.meal;

import app.fittapp.exceptions.ExcessiveNumberOfMeals;
import app.fittapp.exceptions.MealNotFound;
import app.fittapp.exceptions.UserNotFound;
import app.fittapp.meal.model.Meal;
import app.fittapp.meal.model.MealType;
import app.fittapp.meal.repository.MealRepository;
import app.fittapp.meal.service.MealService;
import app.fittapp.user.model.User;
import app.fittapp.user.service.UserService;
import app.fittapp.web.dto.MealRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealServiceUTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MealService mealService;

    //Add New Meal
    @Test
    void throwsExceptionWhen_tryingToAddMoreThanThreeMeals() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .meals(List.of(new Meal(), new Meal(), new Meal()))
                .build();

        MealRequest mealRequest = MealRequest.builder().build();

        assertThrows(ExcessiveNumberOfMeals.class, () -> mealService.addNewMeal(mealRequest, user));
        assertEquals(3, user.getMeals().size());
        verify(mealRepository, never()).save(any());
    }

    @Test
    void whenAddingNewMeal_thenMealAdded() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .meals(new ArrayList<>(List.of(new Meal())))
                .build();

        MealRequest mealRequest = MealRequest.builder()
                .type(MealType.BREAKFAST)
                .photoUrl("image.com")
                .description("Eggs")
                .calories(300)
                .build();

        Meal newMeal = Meal.builder()
                .id(UUID.randomUUID())
                .mealType(mealRequest.getType())
                .photoUrl(mealRequest.getPhotoUrl())
                .description(mealRequest.getDescription())
                .calories(mealRequest.getCalories())
                .user(user)
                .build();

        when(mealRepository.save(any())).thenReturn(newMeal);
        mealService.addNewMeal(mealRequest, user);

        assertEquals(2, user.getMeals().size());
        //assertTrue(user.getMeals().contains(newMeal));
        verify(mealRepository, times(1)).save(any());
    }

    //Get Meal By ID
    @Test
    void throwsExceptionWhen_tryingToGetMealWithNonExistingId() {

        when(mealRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(MealNotFound.class, () -> mealService.getMealById(any()));
    }

    @Test
    void returnsCorrectMealWhen_tryingToGetMealWithExistingId() {

        UUID mealId = UUID.randomUUID();
        Meal meal = Meal.builder()
                .id(mealId)
                .build();

        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));

        Meal actualMeal = mealService.getMealById(mealId);

        assertNotNull(actualMeal);
        assertEquals(meal, actualMeal);

        verify(mealRepository, times(1)).findById(mealId);
    }

    @Test
    void removesMealFromUserMealList_whenDeletingMealById() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .meals(new ArrayList<>(List.of(new Meal(), new Meal(), new Meal())))
                .build();

        Meal mealToRemove = user.getMeals().get(0);

        when(mealRepository.findById(mealToRemove.getId())).thenReturn(Optional.of(mealToRemove));

        mealService.deleteMealById(mealToRemove.getId(), user);

        assertEquals(2, user.getMeals().size());
        assertFalse(user.getMeals().contains(mealToRemove));
        verify(mealRepository, times(1)).deleteById(mealToRemove.getId());
        verify(userService, times(1)).saveUser(user);
    }
}
