package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.elements.AbstractPolyElement.PolyCoordinatePair;
import com.vaadin.flow.component.svg.elements.Circle;
import com.vaadin.flow.component.svg.elements.SvgElement;
import com.vaadin.flow.component.svg.elements.Text;

public class AndNode extends Node {
    private Circle circle;
    private Text text;
    private static final double RADIUS = 50;
    private double x;
    private double y;

    public AndNode(String id, double x, double y) {
        super(id);
        this.x = x;
        this.y = y;

        circle = new Circle(id, RADIUS);
        circle.center(x, y);
        circle.setFillColor("white");
        circle.setStroke("black", 2);

        text = new Text("text", "AND");
        text.move(x - 20, y + 5);
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
        return null;
    }

    @Override
    Text getText() {
        return null;
    }
}
