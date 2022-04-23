package laura.portfolio.libritos.controllers;

import laura.portfolio.libritos.repositories.Book;
import laura.portfolio.libritos.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class BookController {

    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/books")
    String listBooks(Model model){
        List<Book> books = (List<Book>) bookRepository.findAll();
        model.addAttribute("title", "Book list");
        model.addAttribute("books", books);
        return "books/all";
    }

    @GetMapping("/books/new")
    String newBook(Model model){
        Book book = new Book();
        model.addAttribute("book",book);
        model.addAttribute("title", "Create new book");
        return "/books/edit";
    }

    @GetMapping("/books/edit/{id}")
    String editBook(Model model){
        Book book = new Book();
        model.addAttribute("book",book);
        model.addAttribute("title", "Edit book");
        return "/books/edit";
    }

    @PostMapping("/books/new")
    String addBook(@ModelAttribute Book book){
        bookRepository.save(book);
        return "redirect:/books";
    }
}
