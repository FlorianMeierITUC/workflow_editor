package de.ketobi.vaadinspringdemo.main.entities;

import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRequest {
   @JsonProperty("chat_history")
   private List<Message> chatHistory;
   @JsonProperty("system_prompt")
   private String systemPrompt;
   private float temperature;
   private String image;
   private String document;
}
