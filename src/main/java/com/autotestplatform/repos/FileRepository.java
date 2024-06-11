package com.autotestplatform.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autotestplatform.entities.CodeFileEntity;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<CodeFileEntity, Long> {
    Optional<CodeFileEntity> findByIdAndProject_UserId(Long id, String userId);
}