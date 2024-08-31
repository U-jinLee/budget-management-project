package com.example.budget.domain.trade.model;

import com.example.budget.global.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString
@NoArgsConstructor
public class Divergence extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Signal formerSignal;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Signal changeSignal;

    @Enumerated(value = EnumType.STRING)
    private DivergenceType divergenceType;

    @Builder
    public Divergence(Signal formerSignal,
                      Signal changeSignal,
                      DivergenceType divergenceType
                      ) {
        this.formerSignal = formerSignal;
        this.changeSignal = changeSignal;
        this.divergenceType = divergenceType;
    }

}