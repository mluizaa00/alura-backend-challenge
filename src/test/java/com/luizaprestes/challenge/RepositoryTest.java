package com.luizaprestes.challenge;

import com.luizaprestes.challenge.model.persistent.Expense;
import com.luizaprestes.challenge.model.type.ExpenseType;
import com.luizaprestes.challenge.repository.ExpenseRepository;
import com.luizaprestes.challenge.repository.IncomeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class RepositoryTest {

  @Autowired
  private ExpenseRepository expenseRepository;

  @Autowired
  private IncomeRepository incomeRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @Test
  public void testPersist() {
    final Expense expense = Expense.builder()
        .value(1000)
        .description("Test")
        .dateValue(System.currentTimeMillis())
        .id(1)
        .date("")
        .type(ExpenseType.OTHERS)
        .build();

    testEntityManager.persist(expense);
  }

  @Test
  public void hasMoreThanOneExpense() {
    assert expenseRepository.count() > 0;
  }

  @Test
  public void hasNoIncome() {
    assert incomeRepository.count() == 0;
  }

}
