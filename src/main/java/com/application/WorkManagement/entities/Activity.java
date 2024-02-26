package com.application.WorkManagement.entities;

import com.application.WorkManagement.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "hoat_dong"
)
public class Activity {

    @Id
    @Column(
            name = "hd_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(
            name = "hd_loai",
            nullable = false
    )
    @Enumerated(value = EnumType.STRING)
    private ActivityType activityType;
    
    @Column(
            name = "hd_ngay_tao"
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

    @ManyToOne
    @JoinColumn(
            name = "dm_ma_so"
    )
    private Category category;

    @ManyToOne
    @JoinColumn(
            name = "the_ma_so"
    )
    private Card card;

}
