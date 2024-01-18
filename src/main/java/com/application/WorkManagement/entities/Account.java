package com.application.WorkManagement.entities;

import com.application.WorkManagement.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "tai_khoan",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique-email",
                        columnNames = {"tk_email"}
                )
        }
)
public class Account implements UserDetails {

    @Id
    @Column(
            name = "tk_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(
            name = "tk_ho_ten",
            nullable = false,
            length = 50
    )
    private String name;

    @Column(
            name = "tk_email",
            nullable = false
    )
    private String email;

    @Column(
            name = "tk_mat_khau",
            nullable = false
    )
    private String password;

    @Column(
            name = "tk_anh_dai_dien"
    )
    private URI avatar;

    @Column(
            name = "tk_to_chuc"
    )
    private String organization;

    @Column(
            name = "tk_phong_ban"
    )
    private String department;

    @Column(
            name = "tk_chuc_danh"
    )
    private String title;

    @Column(
            name = "tk_ngay_doc_tb",
            nullable = false
    )
    private LocalDateTime notification;

    @Column(
            name = "tk_role",
            nullable = false
    )
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @CreationTimestamp
    @Column(
            name = "tk_ngay_tao"
    )
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "account"
    )
    private List<Workspace> workspaces;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "account"
    )
    private List<WorkspaceMember> workspaceMembers;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "account"
    )
    private List<WorkspaceInviteCode> workspaceInviteCodes;

    @OneToMany(
            mappedBy = "account"
    )
    private List<TableEntity> tableEntities;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "account"
    )
    private List<TableMember> tableMembers;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "account"
    )
    private List<TableStar> tableStars;

    @OneToMany(
            mappedBy = "account"
    )
    private List<Category> categories;

    @OneToMany(
            mappedBy = "account"
    )
    private List<Card> cards;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "account"
    )
    private List<CardMember> cardMembers;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "account"
    )
    private List<CardFollow> cardFollows;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "account"
    )
    private List<Comment> comments;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "account"
    )
    private List<Deadline> deadlines;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(userRole.name())
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
