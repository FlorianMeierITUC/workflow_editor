package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.List;

@Data
public class RAGChatRequest {
    @JsonProperty("chat_history")
    private List<Message> chatHistory;
    private String context;

}
