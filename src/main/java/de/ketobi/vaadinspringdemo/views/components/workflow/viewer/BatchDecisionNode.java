package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.elements.AbstractPolyElement.PolyCoordinatePair;
import com.vaadin.flow.component.svg.elements.SvgElement;
import com.vaadin.flow.component.svg.elements.Text;

public class BatchDecisionNode extends Node {
    double topConnectorX = 50;
    double topConnectorY = 0;
    double bottomConnectorX = 50;
    double bottomConnectorY = 100;

    public BatchDecisionNode(String id) {
        super(id);
    }

    @Override
    public PolyCoordinatePair getTopConnector() {
        return new PolyCoordinatePair(50, 0);
    }

    @Override
    public PolyCoordinatePair getBottomConnector() {
        return new PolyCoordinatePair(50, 100);
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