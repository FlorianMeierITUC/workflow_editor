package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TodoRepositoryTest {

    @InjectMocks
    private TodoRepository todoRepository;

    @Mock
    private TodoRepository mockTodoRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByName() {
        Todo todo = new Todo();
        todo.setName("Test Todo");
        when(mockTodoRepository.findByName("Test Todo")).thenReturn(todo);

        Todo result = todoRepository.findByName("Test Todo");
        assertEquals("Test Todo", result.getName());
    }

    @Test
    public void testDeleteByName() {
        Todo todo = new Todo();
        todo.setName("Test Todo");
        when(mockTodoRepository.findByName("Test Todo")).thenReturn(todo);

        todoRepository.deleteByName("Test Todo");
        Todo result = todoRepository.findByName("Test Todo");
        assertEquals(null, result);
    }
}