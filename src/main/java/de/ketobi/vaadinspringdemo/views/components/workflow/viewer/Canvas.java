package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.Svg;
import com.vaadin.flow.component.svg.elements.Line;
import com.vaadin.flow.component.svg.elements.Rect;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.entities.WorkflowNodeTypes;
import org.bson.types.ObjectId;

import java.util.*;

public class Canvas extends Svg {
    private static final int HORIZONTAL_SPACING = 150;
    private static final int VERTICAL_SPACING = 100;
    private List<WorkflowNode> nodes;
    private Map<String, Node> nodeMap = new HashMap<>();
    private Map<ObjectId, WorkflowNode> workflowNodeMap = new HashMap<>();

    public Canvas(List<WorkflowNode> nodes) {
        super();
        this.nodes = nodes;
        viewbox(0, 0, 1800, 1800);
        setWidth("100%");
        setHeight("1500px");
        Rect background = new Rect("background", 1800, 1800);
        background.setFillColor("white");
        this.add(background);
        WorkflowNode startNode = nodes.stream().filter(n -> n.getType() == WorkflowNodeTypes.START).findFirst().orElseThrow();
        for(WorkflowNode node : nodes) {
            workflowNodeMap.put(node.getId(), node);
        }
        int currentLevel = 0;
        Queue<WorkflowNode> queue = new LinkedList<>();
        queue.add(startNode);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            int currentX = HORIZONTAL_SPACING;
            for (int i = 0; i < levelSize; i++) {
                WorkflowNode node = queue.poll();
                Node svgNode = createNode(node, currentX, currentLevel * VERTICAL_SPACING);
                System.out.println("Draw Node: " + node.getTitle() + " " + node.getType() + " " + currentX + " " + currentLevel * VERTICAL_SPACING);
                nodeMap.put(svgNode.getId(), svgNode);
                this.add(svgNode.getShape());
                this.add(svgNode.getText());

                if (node.getPredecessorNodes() != null && !node.getPredecessorNodes().isEmpty()) {
                    for (ObjectId predecessorId : node.getPredecessorNodes()) {
                        Node parentNode = nodeMap.get(predecessorId.toString());
                        System.out.println("Current Node: " + node.getTitle() + " " + node.getType());
                        Line line = new Line("line", parentNode.getBottomConnector(), svgNode.getTopConnector());
                        line.setStroke("black", 2);
                        System.out.println("Draw Line from Parent Node: " + parentNode.getId() + " to " + svgNode.getId());
                        this.add(line);
                    }
                }

                if(node.getSuccessorNodes() != null && !node.getSuccessorNodes().isEmpty()) {
                    for (ObjectId child : node.getSuccessorNodes()) {
                        System.out.println("Found child Node: " + child);
                        queue.add(workflowNodeMap.get(child));
                        System.out.println("Added child Node: " + workflowNodeMap.get(child).getTitle() + " to queue");
                    }
                }
                currentX += HORIZONTAL_SPACING;
            }

            currentLevel++;
        }
    }

    private Node createNode(WorkflowNode workflowNode, double x, double y) {
        // Create a Node object based on the type of the WorkflowNode
        switch (workflowNode.getType()) {
            case AND:
                return new AndNode(workflowNode.getId().toString(), x, y);
            case OR:
                return new OrNode(workflowNode.getId().toString());
            case UNION:
                return new UnionNode(workflowNode.getId().toString());
            case START:
                return new StartNode(workflowNode.getId().toString(), x, y);
            case END:
                return new EndNode(workflowNode.getId().toString(), x, y, workflowNode.getTitle());
            case USER_DECISION:
                return new UserDecisionNode(workflowNode.getId().toString());
            case USER_ACTION:
                return new UserActionNode(workflowNode.getId().toString());
            case BATCH_DECISION:
                return new BatchDecisionNode(workflowNode.getId().toString());
            case BATCH_ACTION:
                return new BatchActionNode(workflowNode.getId().toString(), x, y, workflowNode.getTitle());
            default:
                throw new IllegalArgumentException("Invalid node type: " + workflowNode.getType());
        }
    }
}