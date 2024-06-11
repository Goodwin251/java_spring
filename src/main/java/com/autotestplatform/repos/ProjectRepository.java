package com.autotestplatform.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autotestplatform.entities.ProjectEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByUserId(String userId);
    Optional<ProjectEntity> findByIdAndUserId(Long id, String userId);
}
