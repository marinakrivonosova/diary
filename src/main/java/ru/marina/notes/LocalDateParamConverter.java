package ru.marina.notes;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateParamConverter implements ParamConverter<LocalDate> {
    @Override
    public LocalDate fromString(final String value) {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public String toString(final LocalDate value) {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(value);
    }
}
