package com.fastcampus.loan.repository;

import com.fastcampus.loan.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {
}
