package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.elements.AbstractPolyElement.PolyCoordinatePair;
import com.vaadin.flow.component.svg.elements.Circle;
import com.vaadin.flow.component.svg.elements.SvgElement;
import com.vaadin.flow.component.svg.elements.Text;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import lombok.Getter;

public class AndNode extends Node {
    private Circle circle;
    @Getter
    private Text text;
    private static final double RADIUS = 20;

    public AndNode(WorkflowNode node) {
        super(node);

        circle = new Circle(id, RADIUS);
        circle.center(x, y);
        circle.setFillColor("white");
        circle.setStroke("black", 2);

        text = new Text("text", node.getTitle());
        text.move(x-RADIUS/2, y-RADIUS/2);
        text.setFillColor("black");
    }

    @Override
    public PolyCoordinatePair getTopConnector() {
        return new PolyCoordinatePair(x, y - RADIUS);
    }

    @Override
    public PolyCoordinatePair getBottomConnector() {
        return new PolyCoordinatePair(x, y + RADIUS);
    }

    @Override
    SvgElement getShape() {
        return circle;
    }
}
