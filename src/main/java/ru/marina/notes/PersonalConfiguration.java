package ru.marina.notes;

import com.netflix.archaius.api.annotations.Configuration;

@Configuration(prefix = "person")
public interface PersonalConfiguration {
    int getDailyCaloriesNorm();
}
