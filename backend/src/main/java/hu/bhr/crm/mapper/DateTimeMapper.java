package hu.bhr.crm.mapper;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public class DateTimeMapper {

    public ZonedDateTime instantToZonedDateTime(Instant instant) {
        return instant == null ? null : ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public Instant zonedDateTimeToInstant(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toInstant();
    }
}
