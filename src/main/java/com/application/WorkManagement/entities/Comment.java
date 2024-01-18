package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.YesNoConverter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "nhan_xet"
)
public class Comment {

    @Id
    @Column(
            name = "nx_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(
            name = "nx_noi_dung",
            nullable = false
    )
    private String comment;


    @Column(
            name = "nx_da_cap_nhat",
            nullable = false
    )
    @Convert(converter = YesNoConverter.class)
    private Boolean update;

    @CreationTimestamp
    @Column(
            name = "nx_ngay_tao"
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
            name = "the_ma_so",
            nullable = false
    )
    private Card card;

}
