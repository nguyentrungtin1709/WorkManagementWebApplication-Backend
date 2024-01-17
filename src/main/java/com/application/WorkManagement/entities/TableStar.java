package com.application.WorkManagement.entities;

import com.application.WorkManagement.entities.CompositePrimaryKeys.TableCompositeId;
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
@Table(
        name = "danh_dau_sao"
)
@IdClass(TableCompositeId.class)
public class TableStar {

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
            name = "bang_ma_so",
            nullable = false
    )
    private TableEntity table;

    @CreationTimestamp
    @Column(
            name = "dds_ngay_tao"
    )
    private LocalDateTime createdAt;

}
