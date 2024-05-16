package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.elements.AbstractPolyElement.PolyCoordinatePair;
import com.vaadin.flow.component.svg.elements.Circle;
import com.vaadin.flow.component.svg.elements.SvgElement;
import com.vaadin.flow.component.svg.elements.Text;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import lombok.Getter;

public class UnionNode extends Node {
    private Circle circle;
    @Getter
    private Text text;
    private static final double RADIUS = 25;

    public UnionNode(WorkflowNode node) {
        super(node);

        circle = new Circle(id, RADIUS);
        circle.center(x+50, y+RADIUS);
        circle.setFillColor("white");
        circle.setStroke("black", 2);

        text = new Text("text", node.getTitle());
        text.move((x-RADIUS/2)+50, (y-RADIUS/2)+RADIUS);
        text.setFillColor("black");
    }

    @Override
    public PolyCoordinatePair getTopConnector() {
        return new PolyCoordinatePair(x+50, y);
    }

    @Override
    public PolyCoordinatePair getBottomConnector() {
        return new PolyCoordinatePair(x+50, y + 2*RADIUS);
    }

    @Override
    SvgElement getShape() {
        return circle;
    }

    @Override
    public void move(double x, double y) {
        this.x = x;
        this.y = y;
        circle.center(x+50, y+RADIUS);
        text.move((x-RADIUS/2)+50, (y-RADIUS/2)+RADIUS);
    }
}