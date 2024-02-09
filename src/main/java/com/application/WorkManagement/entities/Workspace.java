package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
       name = "khong_gian_lam_viec"
)
public class Workspace {

    @Id
    @Column(
            name = "kglv_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

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
            name = "kglv_hinh_nen"
    )
    private URI background;

    @CreationTimestamp
    @Column(
            name = "kglv_ngay_tao"
    )
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so"
    )
    private Account account;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "workspace"
    )
    private List<WorkspaceMember> workspaceMembers;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "workspace"
    )
    private List<WorkspaceInviteCode> workspaceInviteCodes;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "workspace"
    )
    private List<TableEntity> tableEntities;
}
