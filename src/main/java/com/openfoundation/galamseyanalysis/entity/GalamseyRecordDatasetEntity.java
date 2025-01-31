package com.openfoundation.galamseyanalysis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "galamsay_sites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalamseyRecordDatasetEntity {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2") // Generates unique UUIDs
    @Column(name = "id", nullable = false)
    private UUID id;

    private String city;
    private String region;
    private Integer numberOfGalamseySites;
}