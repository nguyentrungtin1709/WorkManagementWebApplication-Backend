package com.application.WorkManagement.entities;

import jakarta.persistence.*;
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
        name = "viec_can_lam"
)
public class ListEntity {

    @Id
    @Column(
            name = "vcl_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(
            name = "vcl_ten",
            nullable = false,
            length = 120
    )
    private String name;

    @CreationTimestamp
    @Column(
            name = "vcl_ngay_tao"
    )
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so"
    )
    private Account account;

    @ManyToOne
    @JoinColumn(
            name = "the_ma_so",
            nullable = false
    )
    private Card card;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "listEntity"
    )
    private List<TaskEntity> taskEntities;

}
