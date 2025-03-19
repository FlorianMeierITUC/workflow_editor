package de.ketobi.vaadinspringdemo.apps.chatdemo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExtractImageResponse {
    @JsonProperty("encoded_image")
    String extracedImage;
}
