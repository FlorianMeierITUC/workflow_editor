package de.ketobi.vaadinspringdemo.apps.chatdemo.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.ketobi.vaadinspringdemo.main.entities.ChatRequest;
import de.ketobi.vaadinspringdemo.main.entities.ChatResponse;
import de.ketobi.vaadinspringdemo.main.entities.ExtractImageResponse;
import de.ketobi.vaadinspringdemo.main.entities.ExtractTextResponse;
import de.ketobi.vaadinspringdemo.apps.chatdemo.services.DataService;
import de.ketobi.vaadinspringdemo.main.services.ChatService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("")
public class ChatController {

    private final ChatService chatService;
    private final DataService dataService;

    @Autowired
    public ChatController(@Qualifier("MainChatService") ChatService chatService, DataService dataService) {
        this.chatService = chatService;
        this.dataService = dataService;
    }

    // @PostMapping("/chat")
    // public Mono<ChatResponse> sendMessage(@RequestBody ChatRequest message) {
    //
    // return chatService.sendMessage(message, ChatResponse.class)
    // .onErrorResume(e -> {
    // e.printStackTrace();
    // return Mono.just(new ChatResponse("Error processing request: " +
    // e.getMessage()));
    // });
    // }

    // @PostMapping("/extract_text")
    // public Mono<ResponseEntity<ExtractTextResponse>>
    // extractText(@RequestParam("file") MultipartFile file) {
    // try {
    // byte[] textBytes = file.getBytes();
    // return dataService.extractText(textBytes, file.getOriginalFilename())
    // .map(response -> ResponseEntity.ok().body(response));
    // } catch (IOException e) {
    // return Mono.just(ResponseEntity.badRequest().build());
    // }

    // }

    // @PostMapping("/extract_image")
    // public Mono<ResponseEntity<ExtractImageResponse>>
    // extractImage(@RequestParam("file") MultipartFile file) {
    // try {
    // byte[] imageBytes = file.getBytes();
    // return dataService.extractImage(imageBytes, file.getOriginalFilename())
    // .map(response -> ResponseEntity.ok().body(response));
    // } catch (IOException e) {
    // return Mono.just(ResponseEntity.badRequest().build());
    // }

    // }

}