package com.luizaprestes.challenge.repository;

import com.luizaprestes.challenge.model.persistent.Income;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

  List<Income> findIncomesByDescriptionContaining(final String description);

}
