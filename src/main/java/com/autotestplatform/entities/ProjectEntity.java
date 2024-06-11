package com.autotestplatform.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String userId; // Додано поле для збереження ідентифікатора користувача

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CodeFileEntity> codeFiles;

    // Гетери і сетери

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CodeFileEntity> getCodeFiles() {
        return codeFiles;
    }

    public void setCodeFiles(List<CodeFileEntity> codeFiles) {
        this.codeFiles = codeFiles;
    }
}