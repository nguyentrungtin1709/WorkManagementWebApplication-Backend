package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

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

    @CreationTimestamp
    @Column(
            name = "kglv_ngay_tao"
    )
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so",
            nullable = false
    )
    private Account account;

    @OneToMany(
            cascade = {CascadeType.ALL},
            mappedBy = "workspace"
    )
    private List<WorkspaceMember> workspaceMembers;

    @OneToMany(
            cascade = {CascadeType.ALL},
            mappedBy = "workspace"
    )
    private List<WorkspaceInviteCode> workspaceInviteCodes;

    @OneToMany(
            cascade = {CascadeType.ALL},
            mappedBy = "workspace"
    )
    private List<TableEntity> tableEntities;
}
