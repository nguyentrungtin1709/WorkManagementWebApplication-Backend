package com.application.WorkManagement.entities;

import com.application.WorkManagement.enums.CardProgress;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    name = "the"
)
public class Card {

    @Id
    @Column(
            name = "the_ma_so"
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(
            name = "the_ten",
            nullable = false,
            length = 120
    )
    private String name;

    @Column(
            name = "the_mo_ta",
            length = 1000
    )
    private String description;

    @Column(
            name = "the_tien_do",
            nullable = false
    )
    @Enumerated(
            value = EnumType.STRING
    )
    private CardProgress progress;

    @Positive
    @Column(
            name = "the_vi_tri",
            nullable = false
    )
    private Integer location;

    @CreationTimestamp
    @Column(
            name = "the_ngay_tao"
    )
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "tk_ma_so"
    )
    private Account account;

    @ManyToOne
    @JoinColumn(
            name = "dm_ma_so",
            nullable = false
    )
    private Category category;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "card"
    )
    private List<CardMember> cardMembers;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "card"
    )
    private List<CardFollow> cardFollows;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "card"
    )
    private List<Comment> comments;

    @OneToOne(
            cascade = {CascadeType.REMOVE},
            mappedBy = "card"
    )
    private Deadline deadline;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "card"
    )
    private List<ListEntity> listEntities;

    @OneToMany(
            cascade = {CascadeType.REMOVE},
            mappedBy = "card"
    )
    private List<Activity> activities;
}
