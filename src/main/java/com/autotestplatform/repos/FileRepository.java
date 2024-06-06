package com.autotestplatform.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autotestplatform.entities.CodeFileEntity;

@Repository
public interface FileRepository extends JpaRepository<CodeFileEntity, Long> {
}
