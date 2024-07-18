package com.br.library_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleBooksClient", url = "https://www.googleapis.com/books/v1")
public interface GoogleBooksClient {
    @GetMapping("/volumes")
    GoogleBooksResponse getBookByIsbn(@RequestParam("q") String isbn);
}
