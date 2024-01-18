package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import lombok.*;
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
        name = "ngay_het_han"
)
public class Deadline {

    @Id
    @Column(
            name = "nhh_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(
            name = "nhh_ngay_het_han",
            nullable = false
    )
    private LocalDateTime deadline;

    @Column(
            name = "nhh_ngay_nhac_nho"
    )
    private LocalDateTime reminderDate;

    @Column(
            name = "nhh_hoan_thanh",
            nullable = false
    )
    @Convert(converter = YesNoConverter.class)
    private Boolean complete;

    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so",
            nullable = false
    )
    private Account account;

    @OneToOne
    @JoinColumn(
            name = "the_ma_so",
            nullable = false
    )
    private Card card;

}
