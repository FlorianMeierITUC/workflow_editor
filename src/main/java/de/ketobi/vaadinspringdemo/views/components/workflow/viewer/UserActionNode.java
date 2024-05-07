package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.elements.AbstractPolyElement.PolyCoordinatePair;
import com.vaadin.flow.component.svg.elements.Rect;
import com.vaadin.flow.component.svg.elements.SvgElement;
import com.vaadin.flow.component.svg.elements.Text;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import lombok.Getter;

public class UserActionNode extends Node {
    private Rect rect;
    @Getter
    private Text text;
    private static final double WIDTH = 100;
    private static final double HEIGHT = 50;

    public UserActionNode(WorkflowNode node) {
        super(node);

        rect = new Rect(id, WIDTH, HEIGHT);
        rect.move(x, y);
        rect.setFillColor("white");
        rect.setStroke("black", 2);

        text = new Text("text", node.getTitle());
        text.move(x + 20, y + 5);
        text.setFillColor("black");
    }

    @Override
    public PolyCoordinatePair getTopConnector() {
        return new PolyCoordinatePair(x + WIDTH/2, y);
    }

    @Override
    public PolyCoordinatePair getBottomConnector() {
        return new PolyCoordinatePair(x + WIDTH/2, y + HEIGHT);
    }

    @Override
    SvgElement getShape() {
        return rect;
    }
}