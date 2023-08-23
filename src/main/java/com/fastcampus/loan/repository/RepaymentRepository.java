package com.fastcampus.loan.repository;

import com.fastcampus.loan.domain.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepaymentRepository extends JpaRepository<Repayment, Long> {
}
