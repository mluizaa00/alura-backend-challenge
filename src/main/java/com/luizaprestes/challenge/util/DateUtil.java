package com.luizaprestes.challenge.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

  private static final DateTimeFormatter FORMATTER;
  private static final ZoneId ZONE_ID;

  static {
    ZONE_ID = ZoneId.of("America/Sao_Paulo");
    FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  }

  private static LocalDate getDate(final long dateValue) {
    return LocalDate.ofInstant(Instant.ofEpochMilli(dateValue), ZONE_ID);
  }

  public static boolean isFromSameDate(final long dateValue, final long year, final long month) {
    final LocalDate dateTime = getDate(dateValue);

    final var yearValue = dateTime.getYear();
    final var monthValue = dateTime.getMonthValue();

    return yearValue == year && monthValue == month;
  }

  public static long parse(final String date) {
    return LocalDateTime.parse(date, FORMATTER)
        .atZone(ZONE_ID)
        .toInstant()
        .toEpochMilli();
  }

}
