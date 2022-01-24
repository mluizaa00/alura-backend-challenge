package com.luizaprestes.challenge.repository;

import com.luizaprestes.challenge.model.persistent.Expense;
import com.luizaprestes.challenge.model.persistent.Income;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  List<Expense> findExpensesByDescriptionContaining(final String description);

}
