package laura.portfolio.libritos;

import laura.portfolio.libritos.repositories.Book;
import laura.portfolio.libritos.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void loadsTheHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }
    @Autowired
    BookRepository bookRepository;

    @Test
    void returnsTheExistingBooks() throws Exception {

        Book book = bookRepository.save(new Book("Harry Potter and the Philosopher's Stone", "J.K. Rowling", "fantasy"));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/all"))
                .andExpect(model().attribute("books", hasItem(book)));
    }

    @Test
    void returnsAFormToAddNewBooks() throws Exception {
        mockMvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("/books/new"));
    }

    @Test
    void allowsToCreateANewBook() throws Exception{
        mockMvc.perform(post("/books/new")
                .param("title","Harry Potter and the Philosopher's Stone")
                .param("author", "J.K. Rowling")
                .param("category", "fantasy")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"))
                ;
        List<Book> existingBooks = (List<Book>) bookRepository.findAll();
        assertThat(existingBooks, contains(allOf(
                hasProperty("title", equalTo("Harry Potter and the Philosopher's Stone")),
                hasProperty("author", equalTo("J.K. Rowling")),
                hasProperty("category", equalTo("fantasy"))
        )));
    }
}
