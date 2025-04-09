package com.lenarsharipov.bookservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookDto {

    private Long id;

    @NotBlank(message = "Vendor code is required")
    @Size(min = 3, max = 50, message = "Vendor code must be between 3 and 50 characters")
    private String vendorCode;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @Min(value = 1800, message = "Publication year must be after 1800")
    @Max(value = 2100, message = "Publication year must be before 2100")
    private Integer publicationYear;

    @Size(max = 50, message = "Brand must be less than 50 characters")
    private String brand;

    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    @DecimalMax(value = "10000.00", message = "Price must be less than 10000.00")
    private Double price;
}
