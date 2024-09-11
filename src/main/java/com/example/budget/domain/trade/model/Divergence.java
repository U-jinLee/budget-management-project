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
@Table(name = "divergence")
@NoArgsConstructor
public class Divergence extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Signal formerSignal;

    @Enumerated(value = EnumType.STRING)
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