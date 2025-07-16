package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExtractImageResponse {
    @JsonProperty("encoded_image")
    String extracedImage;
}
