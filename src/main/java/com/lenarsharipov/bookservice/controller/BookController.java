package com.lenarsharipov.bookservice.controller;

import com.lenarsharipov.bookservice.dto.BookDto;
import com.lenarsharipov.bookservice.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private static final List<Integer> PAGE_SIZES = List.of(5, 10, 20, 50);

    private final BookService bookService;

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(
            Exception ex,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error",
                "Error occurred: " + ex.getMessage());
        return "redirect:/books";
    }

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer year,
            Model model) {

        Page<BookDto> bookPage = bookService.getAllBooks(page, size, title, brand, year);

        int current = bookPage.getNumber() + 1;
        int begin = Math.max(1, current - 2);
        int end = Math.min(begin + 4, bookPage.getTotalPages());

        model.addAttribute("books", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("titleFilter", title);
        model.addAttribute("brandFilter", brand);
        model.addAttribute("yearFilter", year);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);

        return "books";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new BookDto());
        return "edit-book";
    }

    @PostMapping("/save")
    public String saveBook(
            @Valid @ModelAttribute("book") BookDto bookDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "edit-book";
        }

        bookService.saveBook(bookDto);
        redirectAttributes.addFlashAttribute(
                "message",
                "Book saved successfully"
        );

        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model
    ) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    return "edit-book";
                })
                .orElse("redirect:/books");
    }

    @PostMapping("/update/{id}")
    public String updateBook(
            @PathVariable Long id,
            @Valid @ModelAttribute("book") BookDto bookDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "edit-book";
        }

        bookService.updateBook(id, bookDto)
                .ifPresentOrElse(
                        book -> redirectAttributes.addFlashAttribute(
                                "message",
                                "Book updated successfully"),
                        () -> redirectAttributes.addFlashAttribute(
                                "error",
                                "Book not found"));

        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        if (bookService.deleteBook(id)) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Book deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Book not found");
        }
        return "redirect:/books";
    }
}
