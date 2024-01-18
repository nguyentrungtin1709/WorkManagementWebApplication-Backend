package com.application.WorkManagement.entities;

import com.application.WorkManagement.enums.TableScope;
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
        name = "bang",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "tablename_workspace_unique",
                        columnNames = {"bang_ten", "kglv_ma_so"}
                )
        }
)
public class TableEntity {

    @Id
    @Column(
            name = "bang_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(
            name = "bang_ten",
            nullable = false,
            length = 120
    )
    private String name;

    @Column(
            name = "bang_mo_ta",
            length = 1000
    )
    private String description;

    @Column(
            name = "bang_pham_vi",
            nullable = false
    )
    @Enumerated(
            value = EnumType.STRING
    )
    private TableScope tableScope;

    @Column(
            name = "bang_hinh_nen"
    )
    private URI background;

    @CreationTimestamp
    @Column(
            name = "bang_ngay_tao"
    )
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so"
    )
    private Account account;

    @ManyToOne
    @JoinColumn(
            name = "kglv_ma_so",
            nullable = false
    )
    private Workspace workspace;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "table"
    )
    private List<TableMember> tableMembers;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "table"
    )
    private List<TableStar> tableStars;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "table"
    )
    private List<Category> categories;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "table"
    )
    private List<Activity> activities;
}
