package com.br.library_api.client;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GoogleBookVolumeInfo {
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
}
