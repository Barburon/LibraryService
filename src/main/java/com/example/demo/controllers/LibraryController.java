package com.example.demo.controllers;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.entity.Library;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {

    RestTemplate restTemplate;

    public LibraryController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/library")
    public Library getLibrary(@RequestParam("bookId") Long bookId, @RequestParam("authorId") Long authorId) {

        Book book = restTemplate.getForObject("http://Book-Service/api/books/find/" + bookId, Book.class);
        Author author = restTemplate.getForObject("http://Author-Service/api/authors/find/" + authorId, Author.class);

        return Library.builder()
                .book(book)
                .author(author)
                .build();
    }

    @GetMapping("/library/books")
    public List<Book> getBooks() {
        ResponseEntity<List<Book>> booksList = restTemplate.exchange("http://Book-Service/api/books/", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Book>>() {
                });
        return booksList.getBody();
    }

    @GetMapping("/library/authors")
    public List<Author> getAuthors() {
        ResponseEntity<List<Author>> authorsList = restTemplate.exchange("http://Author-Service/api/authors/", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Author>>() {
                });
        return authorsList.getBody();
    }
}
