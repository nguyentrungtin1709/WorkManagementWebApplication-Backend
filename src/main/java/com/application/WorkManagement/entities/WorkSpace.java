package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
       name = "khong_gian_lam_viec"
)
public class WorkSpace {

    @Id
    @GeneratedValue
    @Column(
            name = "kglv_ma_so"
    )
    private Long id;

    @Column(
            name = "kglv_ten",
            nullable = false,
            length = 120
    )
    private String name;

    @Column(
            name = "kglv_mo_ta",
            length = 1000
    )
    private String description;

    @Column(
            name = "kglv_ngay_tao"
    )
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so",
            nullable = false,
            foreignKey = @ForeignKey(name = "kglv_fk_tai_khoan")
    )
    private Account account;
}
