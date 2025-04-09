package com.lenarsharipov.bookservice.repository;

import com.lenarsharipov.bookservice.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterBookRepository {

    Page<Book> findAllByFilter(String title, String brand, Integer year, Pageable pageable);
}
