package com.application.WorkManagement.entities;

import com.application.WorkManagement.entities.CompositePrimaryKeys.WorkspaceMemberId;
import com.application.WorkManagement.enums.WorkspaceRole;
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
@IdClass(WorkspaceMemberId.class)
@Table(
        name = "ma_moi_kglv"
)
public class WorkspaceInviteCode {

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
            name = "mmkglv_vai_tro",
            nullable = false
    )
    private WorkspaceRole workspaceRole;

    @Column(
            name = "mmkglv_ma_moi",
            nullable = false
    )
    private UUID inviteCode;

    @CreationTimestamp
    @Column(
            name = "mmkglv_ngay_tao"
    )
    private LocalDateTime createdAt;
}
