package com.application.WorkManagement.entities;

import com.application.WorkManagement.entities.CompositePrimaryKeys.TableCompositeId;
import com.application.WorkManagement.enums.TableRole;
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
        name = "thanh_vien_bang"
)
@IdClass(TableCompositeId.class)
public class TableMember {

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

    @Column(
            name = "tvb_vai_tro",
            nullable = false
    )
    private TableRole tableRole;

    @CreationTimestamp
    @Column(
            name = "tvb_ngay_tao"
    )
    private LocalDateTime createdAt;
}
