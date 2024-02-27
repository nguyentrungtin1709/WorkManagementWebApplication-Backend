package com.application.WorkManagement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "thu_vien_anh"
)
public class ImageGallery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "tva_ma_so"
    )
    private UUID uuid;

    @Column(
            nullable = false,
            name = "tva_dia_chi_anh"
    )
    private URI imageUri;

    @CreationTimestamp
    @Column(
            name = "tva_ngay_tao"
    )
    private LocalDateTime createdAt;

}
