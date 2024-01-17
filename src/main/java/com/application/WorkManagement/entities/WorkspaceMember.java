package com.application.WorkManagement.entities;

import com.application.WorkManagement.entities.CompositePrimaryKeys.WorkspaceMemberId;
import com.application.WorkManagement.enums.WorkspaceRole;
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
        name = "thanh_vien_kglv"
)
@IdClass(WorkspaceMemberId.class)
public class WorkspaceMember {

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
            name = "kglv_ma_so",
            nullable = false
    )
    private Workspace workspace;

    @Enumerated(
            value = EnumType.STRING
    )
    @Column(
            name = "tvkglv_vai_tro",
            nullable = false
    )
    private WorkspaceRole workspaceRole;


    @CreationTimestamp
    @Column(
            name = "tvkglv_ngay_tao"
    )
    private LocalDateTime createdAt;
}
