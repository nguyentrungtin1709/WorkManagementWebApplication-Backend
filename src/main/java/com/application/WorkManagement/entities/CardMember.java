package com.application.WorkManagement.entities;

import com.application.WorkManagement.entities.CompositePrimaryKeys.CardCompositeId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CardCompositeId.class)
@Table(
        name = "thanh_vien_the"
)
public class CardMember {

    @Id
    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so",
            nullable = false
    )
    private Account account;

    @Id
    @ManyToOne
    @JoinColumn(
            name = "the_ma_so",
            nullable = false
    )
    private Card card;

    @CreationTimestamp
    @Column(
            name = "tvt_ngay_tao"
    )
    private LocalDateTime createdAt;

}
