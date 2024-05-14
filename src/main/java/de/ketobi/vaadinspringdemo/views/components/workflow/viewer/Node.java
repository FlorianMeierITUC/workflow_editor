package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.elements.*;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import lombok.Getter;
import lombok.Setter;

public abstract class Node extends SvgElement {

    @Getter
    @Setter
    protected double x;
    @Getter
    @Setter
    protected double y;
    @Getter
    @Setter
    protected int xLevel;
    @Getter
    @Setter
    protected int yLevel;
    @Getter
    @Setter
    protected WorkflowNode node;
    @Getter
    @Setter
    protected String id;

    //TODO all nodes should have the connector above them with an arrow pointing downwards
    public Node(WorkflowNode node) {
        this(node, 0, 0);
    }

    public Node(WorkflowNode node, double x, double y) {
        super(node.getId().toString());
        this.node = node;
        this.id = node.getId().toString();
        this.x = x;
        this.y = y;
    }

    abstract AbstractPolyElement.PolyCoordinatePair getTopConnector();
    abstract AbstractPolyElement.PolyCoordinatePair getBottomConnector();
    abstract SvgElement getShape();
    abstract Text getText();
    @Override
    abstract public void move(double x, double y);
}
