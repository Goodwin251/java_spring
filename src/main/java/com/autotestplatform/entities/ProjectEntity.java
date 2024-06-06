package com.autotestplatform.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<CodeFileEntity> codeFiles;

    // Getters and setters
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

    public List<CodeFileEntity> getCodeFiles() {
        return codeFiles;
    }

    public void setCodeFiles(List<CodeFileEntity> codeFiles) {
        this.codeFiles = codeFiles;
    }
}
