package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import java.util.Map;

@Data
public class OnepagerResponse {
    @JsonProperty("onepager_response")
    private Map<Integer, String> onepagerResponse;

}
