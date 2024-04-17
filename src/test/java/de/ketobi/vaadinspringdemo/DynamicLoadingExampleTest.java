package de.ketobi.vaadinspringdemo;

import de.ketobi.vaadinspringdemo.views.DynamicLoadingExample;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicLoadingExampleTest {
    // Loading dynamic content updates the text of the dynamicContent Div
    @Test
    public void test_loading_dynamic_content_updates_text() {
        DynamicLoadingExample example = new DynamicLoadingExample();
        example.loadDynamicContent();
        assertEquals("This is dynamic content", example.dynamicContent.getText());
    }
}