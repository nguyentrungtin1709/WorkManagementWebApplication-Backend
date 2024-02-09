package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "hinh_dai_dien"
)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "hdd_ma_so"
    )
    private UUID uuid;

    @Column(
            name = "hdd_ten",
            nullable = false
    )
    private String originalFileName;

    @Column(
            name = "hdd_kieu",
            nullable = false
    )
    private String contentType;

    @Column(
            name = "hdd_kich_thuoc",
            nullable = false
    )
    private Long size;

}
