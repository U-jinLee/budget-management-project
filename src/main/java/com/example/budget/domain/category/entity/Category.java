package com.example.budget.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
@Entity
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "selected_count", nullable = false)
    private Integer selectedCount;

    @Builder
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.selectedCount = 0;
    }

}