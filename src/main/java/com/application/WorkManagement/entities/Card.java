package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "the"
)
public class Card {

    @Id
    @GeneratedValue
    @Column(
            name = "the_ma_so"
    )
    private Long id;

    @Column(
            name = "the_ten",
            nullable = false,
            length = 120
    )
    private String name;

    @Column(
            name = "the_mo_ta",
            length = 1000
    )
    private String description;

    @Positive
    @Column(
            name = "the_vi_tri",
            nullable = false
    )
    private Integer location;

    @CreationTimestamp
    @Column(
            name = "the_ngay_tao"
    )
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so",
            nullable = false
    )
    private Account account;

    @ManyToOne
    @JoinColumn(
            name = "dm_ma_so",
            nullable = false
    )
    private Category category;

}
