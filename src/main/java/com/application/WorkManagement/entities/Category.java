package com.application.WorkManagement.entities;

import com.application.WorkManagement.enums.CategoryColor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    @Column(
            name = "dm_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(
            name = "dm_ten",
            nullable = false,
            length = 120
    )
    private String name;

    @Column(
            name = "dm_vi_tri",
            nullable = false
    )
    @Positive
    private Integer position;

    @Column(
            name = "dm_mau_sac",
            nullable = false
    )
    @Enumerated(
            value = EnumType.STRING
    )
    private CategoryColor color;

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
            name = "tk_ma_so"
    )
    private Account account;

    @ManyToOne
    @JoinColumn(
            name = "bang_ma_so",
            nullable = false
    )
    private TableEntity table;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "category"
    )
    private List<Card> cards;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "category"
    )
    private List<Activity> activities;

}
