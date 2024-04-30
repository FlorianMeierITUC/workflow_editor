package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.elements.*;

public abstract class Node extends SvgElement {
    public Node(String id) {
        super(id);
    }

    abstract AbstractPolyElement.PolyCoordinatePair getTopConnector();
    abstract AbstractPolyElement.PolyCoordinatePair getBottomConnector();
    abstract SvgElement getShape();
    abstract Text getText();
}
