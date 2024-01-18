package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.type.TrueFalseConverter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "cong_viec"
)
public class TaskEntity {

    @Id
    @Column(
            name = "cv_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(
            name = "cv_ten",
            nullable = false,
            length = 120
    )
    private String name;


    @Column(
            name = "cv_hoan_thanh",
            nullable = false
    )
    @Convert(converter = TrueFalseConverter.class)
    private Boolean complete;

    @CreationTimestamp
    @Column(
            name = "cv_ngay_tao"
    )
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so"
    )
    private Account account;

    @ManyToOne
    @JoinColumn(
            name = "vcl_ma_so",
            nullable = false
    )
    private ListEntity listEntity;

}
