package com.tenpo.apirest.repository;

import com.tenpo.apirest.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiRestRepository extends JpaRepository<History, Long> {
    @Query("SELECT h FROM History h")
    List<History> findHistory();
}

