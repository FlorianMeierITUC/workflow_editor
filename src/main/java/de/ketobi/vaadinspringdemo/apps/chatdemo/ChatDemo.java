package de.ketobi.vaadinspringdemo.apps.chatdemo;

import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.apps.chatdemo.services.ChatService;
import de.ketobi.vaadinspringdemo.apps.chatdemo.services.DataService;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.entities.ChatRequest;
import de.ketobi.vaadinspringdemo.main.entities.ChatResponse;
import de.ketobi.vaadinspringdemo.main.entities.Message;
import de.ketobi.vaadinspringdemo.main.entities.ExtractImageResponse;
import de.ketobi.vaadinspringdemo.main.entities.ExtractTextResponse;
import de.ketobi.vaadinspringdemo.main.login.Login;

import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.parser.Parser;
import org.commonmark.node.Node;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import java.util.List;
import java.util.ArrayList;

@Route(value = "chatdemo", layout = MainLayout.class)
@PageTitle("Chat Demo")
public class ChatDemo extends VerticalLayout implements BeforeEnterObserver {

    private final ChatService chatService;
    private final DataService dataService;
    private List<Message> chatHistory = new ArrayList<>();
    private static final String USER_EMOJI = "🧑"; // or "👤", "👨", etc.
    private static final String ASSISTANT_EMOJI = "🤖"; // Robot
    private static final HtmlRenderer MD_RENDERER = HtmlRenderer.builder().build();
    private static final Parser MD_PARSER = Parser.builder().build();
    private final VerticalLayout chatHistoryLayout = new VerticalLayout();
    private final NumberField temperatureField = new NumberField("Temperature");
    private final TextArea prompt = new TextArea("System Prompt");
    private final TextArea message = new TextArea("Type your message...");
    private final MemoryBuffer pdfBuffer = new MemoryBuffer();
    private final Upload pdfUpload = new Upload(pdfBuffer);
    private String extractedText = "";

    private final MemoryBuffer imageBuffer = new MemoryBuffer();
    private final Upload imageUpload = new Upload(imageBuffer);
    private String extractedImage = "";

    public ChatDemo(ChatService chatService, DataService dataService) {
        this.chatService = chatService;
        this.dataService = dataService;
        setSizeFull(); // fill the entire page

        // Title & subtitle
        H3 title = new H3("ITUKI Chat");
        H4 subtitle = new H4("Chat with ITUKI");

        // 1) Prompt & Temperature
        prompt.setPlaceholder("Enter a system instruction or context here...");
        prompt.setWidthFull();

        temperatureField.setMin(0);
        temperatureField.setMax(1);
        temperatureField.setStep(0.1);
        temperatureField.setValue(0.7);
        temperatureField.setHelperText("Range: 0.0 - 1.0");
        temperatureField.setWidth("150px");

        FormLayout configLayout = new FormLayout();
        configLayout.addFormItem(prompt, "System Prompt");
        configLayout.addFormItem(temperatureField, "Temperature");
        configLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2));

        // 2) PDF Upload
        H4 pdfLabel = new H4("Upload a PDF document:");
        pdfUpload.setAcceptedFileTypes("application/pdf");
        pdfUpload.setMaxFiles(1);
        pdfUpload.setDropLabel(new Span("Drop your PDF here or click to upload"));
        pdfUpload.addSucceededListener(event -> handlePdfUpload(event.getFileName()));
        VerticalLayout pdfLayout = new VerticalLayout(pdfLabel, pdfUpload);
        pdfLayout.setPadding(false);

        // 3) Image Upload
        H4 imageLabel = new H4("Upload an Image:");
        imageUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imageUpload.setMaxFiles(1);
        imageUpload.setDropLabel(new Span("Drop your image here or click to upload"));
        imageUpload.addSucceededListener(event -> handleImageUpload(event.getFileName()));
        VerticalLayout imageLayout = new VerticalLayout(imageLabel, imageUpload);
        imageLayout.setPadding(false);

        // Horizontal layout for file uploads
        HorizontalLayout fileUploadsLayout = new HorizontalLayout(pdfLayout, imageLayout);
        fileUploadsLayout.setSpacing(true);
        fileUploadsLayout.setWidthFull();

        // 4) Chat input
        message.setPlaceholder("Type your message...");
        message.setWidthFull();
        ChatButton submitButton = new ChatButton();

        // 5) Chat history layout: make it scrollable
        chatHistoryLayout.setHeight("800px");
        chatHistoryLayout.setWidthFull();
        chatHistoryLayout.getStyle().set("overflow", "auto");
        chatHistoryLayout.setPadding(false);
        chatHistoryLayout.setSpacing(false);

        // Combine everything for chat
        VerticalLayout chatLayout = new VerticalLayout(message, submitButton, chatHistoryLayout);
        chatLayout.setSpacing(true);
        chatLayout.setPadding(false);
        chatLayout.setWidthFull();

        // Final arrangement
        add(title, subtitle, configLayout, fileUploadsLayout, chatLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (UserService.getCurrentUser() == null) {
            event.forwardTo(Login.class);
        }
    }

    private String markdownToHtml(String markdown) {
        Node document = MD_PARSER.parse(markdown);
        return MD_RENDERER.render(document);
    }

    private void addMessageWithEmoji(String sender, String content, boolean isAssistant, boolean isMarkdown) {
        // 1) A horizontal layout to hold the emoji + bubble
        HorizontalLayout container = new HorizontalLayout();
        container.setWidthFull();
        container.setSpacing(true);
        container.setPadding(false);

        // Decide which emoji to use
        String emoji = isAssistant ? ASSISTANT_EMOJI : USER_EMOJI;
        Span emojiSpan = new Span(emoji);
        emojiSpan.getStyle().set("font-size", "24px"); // enlarge emoji if you like
        emojiSpan.getStyle().set("margin", "6px");

        // 2) Create bubble
        Div bubble = new Div();
        if (isMarkdown) {
            String html = markdownToHtml(content);
            bubble.getElement().setProperty("innerHTML",
                    "<b>" + sender + ":</b><br>" + html);
        } else {
            bubble.getElement().setProperty("innerHTML",
                    "<b>" + sender + ":</b><br>" + content);
        }

        // Style bubble inline
        styleBubble(bubble, isAssistant);

        // 3) If it's assistant, place emoji on the left
        // If it's user, place emoji on the right
        if (isAssistant) {
            container.add(emojiSpan, bubble);
        } else {
            container.add(bubble, emojiSpan);
            // container.setJustifyContentMode(JustifyContentMode.END);
        }

        // 4) Add to the chat layout, scroll to bottom
        chatHistoryLayout.add(container);
        chatHistoryLayout.getElement().callJsFunction(
                "scrollTop",
                chatHistoryLayout.getElement().getProperty("scrollHeight"));
    }

    // -------------------------------------------------------------
    // Basic bubble styling (inline, no CSS file)
    // -------------------------------------------------------------
    private void styleBubble(Div bubble, boolean isAssistant) {
        bubble.getStyle().set("border-radius", "8px");
        bubble.getStyle().set("padding", "8px 12px");
        bubble.getStyle().set("margin", "6px 0");
        bubble.getStyle().set("max-width", "70%");
        bubble.getStyle().set("box-shadow", "0 2px 2px rgba(0,0,0,0.2)");

        if (isAssistant) {
            bubble.getStyle().set("background-color", "#F2F2F2"); // gray
            bubble.getStyle().set("color", "black");
        } else {
            bubble.getStyle().set("background-color", "#DCF8C6"); // greenish
            bubble.getStyle().set("color", "black");
        }
    }

    // -------------------------------------------------------------
    // Handle Pdf Upload
    // -------------------------------------------------------------
    private void handlePdfUpload(String originalFileName) {
        try {
            byte[] pdfBytes = pdfBuffer.getInputStream().readAllBytes();
            //
            extractedText = dataService.extractText(pdfBytes, originalFileName)
                    .map(ExtractTextResponse::getText)
                    // Blocking is not recommended
                    .block();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    // Handle Image Upload
    // -------------------------------------------------------------
    private void handleImageUpload(String originalFileName) {
        try {
            byte[] imageBytes = imageBuffer.getInputStream().readAllBytes();
            //
            extractedImage = dataService.extractImage(imageBytes, originalFileName)
                    .map(ExtractImageResponse::getExtracedImage)
                    // Blocking is not recommended
                    .block();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ChatButton extends Button {

        public ChatButton() {

            setText("Start Chat");
            addSingleClickListener(e -> {
                UI ui = UI.getCurrent();
                if (ui == null) {
                    return;
                }

                ChatRequest chatRequest = new ChatRequest();
                String chatInput = message.getValue();
                String systemPrompt = prompt.getValue();
                float temperature = temperatureField.getValue().floatValue();

                List<Message> Messages = List.of(new Message("user", chatInput));
                System.out.println("Chat input: " + chatInput);

                chatRequest.setSystemPrompt(systemPrompt);
                chatRequest.setChatHistory(Messages);
                chatRequest.setTemperature(temperature);
                chatRequest.setImage(extractedImage);
                chatRequest.setDocument(extractedText);

                addMessageWithEmoji("User", chatInput, /* isAssistant= */false, /* isMarkdown= */true);

                chatService.sendMessage(chatRequest, ChatResponse.class)
                        .map(ChatResponse::getResponse)
                        .defaultIfEmpty("No response")
                        .subscribe(response -> {
                            ui.access(() -> {
                                // 3) Show assistant's emoji bubble with Markdown
                                // Add to chat history
                                chatHistory.add(new Message("assistant", response));
                                addMessageWithEmoji("ITUKI", response, /* isAssistant= */true, /* isMarkdown= */true);
                                message.setValue("");
                                ui.push();
                            });
                        });

            });
        }
    }
}