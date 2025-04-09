package com.lenarsharipov.bookservice.service;

import com.lenarsharipov.bookservice.dto.BookDto;
import com.lenarsharipov.bookservice.model.Book;
import com.lenarsharipov.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public Page<BookDto> getAllBooks(
            int page,
            int size,
            String title,
            String brand,
            Integer year
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAllByFilter(title, brand, year, pageable)
                .map(this::convertToDto);
    }

    public Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public void saveBook(BookDto bookDto) {
        Book book = convertToEntity(bookDto);
        bookRepository.save(book);
    }

    @Transactional
    public Optional<BookDto> updateBook(Long id, BookDto bookDto) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    updateEntityFromDto(existingBook, bookDto);
                    Book updatedBook = bookRepository.save(existingBook);
                    return convertToDto(updatedBook);
                });
    }

    @Transactional
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private BookDto convertToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setVendorCode(book.getVendorCode());
        dto.setTitle(book.getTitle());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setBrand(book.getBrand());
        dto.setStock(book.getStock());
        dto.setPrice(book.getPrice());
        return dto;
    }

    private Book convertToEntity(BookDto dto) {
        Book book = new Book();
        updateEntityFromDto(book, dto);
        return book;
    }

    private void updateEntityFromDto(Book book, BookDto dto) {
        book.setVendorCode(dto.getVendorCode());
        book.setTitle(dto.getTitle());
        book.setPublicationYear(dto.getPublicationYear());
        book.setBrand(dto.getBrand());
        book.setStock(dto.getStock());
        book.setPrice(dto.getPrice());
    }
}


