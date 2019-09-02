package ru.marina.notes;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeParamConverter implements ParamConverter<LocalTime> {
    @Override
    public String toString(final LocalTime value) {
        return DateTimeFormatter.ISO_LOCAL_TIME.format(value);
    }

    @Override
    public LocalTime fromString(final String value) {
        return LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
