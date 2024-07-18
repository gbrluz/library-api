package com.br.library_api.client;

import lombok.Getter;

import java.util.List;

@Getter
public class GoogleBooksResponse {
    private List<GoogleBookItem> items;

}
