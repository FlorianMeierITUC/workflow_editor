package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.elements.AbstractPolyElement.PolyCoordinatePair;
import com.vaadin.flow.component.svg.elements.Rect;
import com.vaadin.flow.component.svg.elements.SvgElement;
import com.vaadin.flow.component.svg.elements.SvgType;
import com.vaadin.flow.component.svg.elements.Text;
import elemental.json.JsonValue;
import lombok.Getter;

public class EndNode extends Node {
    private Rect rect;
    @Getter
    private Text text;
    private static final double WIDTH = 100;
    private static final double HEIGHT = 50;
    private double x;
    private double y;

    public EndNode(String id, double x, double y, String title) {
        super(id);
        this.x = x;
        this.y = y;

        rect = new Rect(id, WIDTH, HEIGHT);
        rect.move(x, y);
        rect.setFillColor("white");
        rect.setStroke("black", 2);

        text = new Text("text", title);
        text.move(x + 20, y + 5);
        text.setFillColor("black");
        text.setFontSize("20");
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