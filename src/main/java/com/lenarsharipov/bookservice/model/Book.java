package com.lenarsharipov.bookservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_code", nullable = false)
    private String vendorCode;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "publication_year")
    private Integer publicationYear;
    private String brand;
    private Integer stock;
    private Double price;
}
