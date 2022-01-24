package com.luizaprestes.challenge.model.persistent;

import com.luizaprestes.challenge.model.type.ExpenseType;
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
@Entity(name = "challenge_expense")
@NoArgsConstructor
@AllArgsConstructor
public final class Expense implements Serializable {

  @Id
  private long id;

  private ExpenseType type;

  private String description;
  private long value;

  private String date;
  private long dateValue;

  private Calendar getCalendar() {
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
