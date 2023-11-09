package com.example.budget.domain.statistics.entity;

import com.example.budget.domain.statistics.model.Type;
import com.example.budget.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.ToString;

@ToString
@Entity
public class ExpenditureStatistics extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "percent", nullable = false)
    private Integer percent;

    @Enumerated(EnumType.STRING)
    private Type type;

}