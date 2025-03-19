package de.ketobi.vaadinspringdemo.apps.chatdemo.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message{
        private String role;
        private String content;

        public Message(String role, String content) {
                this.role = role;
                this.content = content;
        }

}