package com.luizaprestes.challenge.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity(name = "challenge_income")
@NoArgsConstructor
@AllArgsConstructor
public final class Income implements Serializable {

  @Id
  private long id;

  @NotBlank
  private String description;
  @Min(1)
  private long value;

  @NotBlank
  private String date;
  private long dateValue;

  public Calendar getCalendar() {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(dateValue));

    return calendar;
  }

  public boolean isFromSameDate(final long year, final long month) {
    final Calendar calendar = getCalendar();

    final int yearValue = calendar.get(Calendar.YEAR);
    final int monthValue = calendar.get(Calendar.MONTH);

    return yearValue == year && monthValue == month;
  }

}
