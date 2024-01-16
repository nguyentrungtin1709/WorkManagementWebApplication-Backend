package com.application.WorkManagement.entities;

import com.application.WorkManagement.enums.Role;
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
    @GeneratedValue
    @Column(
            name = "tk_ma_so"
    )
    private Long id;

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

    @Enumerated(
            value = EnumType.STRING
    )
    @Column(
            name = "tk_role",
            nullable = false
    )
    private Role role;


    @Column(
            name = "tk_ngay_tao",
            nullable = false
    )
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(
            cascade = {CascadeType.ALL},
            mappedBy = "account",
            fetch = FetchType.LAZY
    )
    private List<WorkSpace> workSpaces;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(role.name())
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
