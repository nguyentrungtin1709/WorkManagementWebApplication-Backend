package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "danh_muc",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "category_name_table_unique",
                        columnNames = {"bang_ma_so", "dm_ten"}
                )
        }
)
public class Category {

    @Id
    @GeneratedValue
    @Column(
            name = "dm_ma_so"
    )
    private Long id;

    @Column(
            name = "dm_ten",
            nullable = false,
            length = 120
    )
    private String name;

    @PositiveOrZero
    @Column(
            name = "dm_so_luong_the",
            nullable = false
    )
    private Integer numberOfCards;

    @CreationTimestamp
    @Column(
            name = "dm_ngay_tao"
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
            name = "bang_ma_so",
            nullable = false
    )
    private TableEntity table;

    @OneToMany(
            cascade = {CascadeType.ALL},
            mappedBy = "category"
    )
    private List<Card> cards;

}
