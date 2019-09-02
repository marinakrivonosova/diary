package ru.marina.notes;

import com.netflix.archaius.api.annotations.Configuration;
import com.netflix.archaius.api.annotations.DefaultValue;

@Configuration(prefix = "database")
public interface DBConfiguration {
    String getConnectionString();

    @DefaultValue("admin")
    String getUser();

    String getPassword();
}
