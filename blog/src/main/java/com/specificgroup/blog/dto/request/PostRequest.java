package com.specificgroup.blog.dto.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
public class PostRequest {

    @NotBlank
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9 ]+", message = "Title can contain: letters, numbers and spaces")
    @Size(min = 5, max = 100, message = "Title min size is 5 symbols and max size is 100 symbols")
    String title;

    @NotBlank
    @Size(min = 5, max = 2000, message = "Text min size is 5 symbols and max size is 2000 symbols")
    String text;
}