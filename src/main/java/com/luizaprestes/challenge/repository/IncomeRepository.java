package com.luizaprestes.challenge.repository;

import com.luizaprestes.challenge.model.Income;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, UUID> {

}
