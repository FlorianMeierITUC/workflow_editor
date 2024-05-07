package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.Svg;
import com.vaadin.flow.component.svg.elements.Rect;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.entities.WorkflowNodeTypes;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.*;

public class WorkflowView extends Svg {
    //All nodes in the workflow
    @Getter
    private List<Node> nodes = new ArrayList<>();
    private static final int HORIZONTAL_SPACING = 150;
    private static final int VERTICAL_SPACING = 100;
    private static final int HORIZONTAL_UNIT = 250;
    private static final int VERTICAL_UNIT = 150;

    public WorkflowView(List<WorkflowNode> nodes) {
        super();
        for(WorkflowNode node : nodes) {
            this.nodes.add(createNode(node));
        }
        orderNodesIntoYLevels();
        setPositionOfNodes();
        double width = getWidthOfCanvas();
        double height = getHeightOfCanvas();
        viewbox(0, 0, width, height);
        setWidth(width+"px");
        setHeight(height+"px");
        Rect background = new Rect("background", width, height);
        background.setFillColor("white");
        this.add(background);
        drawNodes();
        drawConnections();
    }

    private void orderNodesIntoYLevels(){
        Queue<Node> nodesOnTheCurrentLevel = new LinkedList<>();
        Queue<Node> nodesOnTheNextLevel = new LinkedList<>();
        // Create a map of the node id to the Node object
        Map<ObjectId, Node> idToNode = new HashMap<>();
        for(Node node : nodes) {
            idToNode.put(node.getNode().getId(), node);
        }
        Map<Node, Set<Node>> ancestors = new HashMap<>();

        Node startNode = nodes.stream().filter(n -> n.getNode().getType() == WorkflowNodeTypes.START).findFirst().orElseThrow();
        //check if the start node is present and if it is the only one in the stream
        if (nodes.stream().filter(n -> n.getNode().getType() == WorkflowNodeTypes.START).count() != 1) {
            throw new IllegalArgumentException("There must be exactly one start node in the workflow");
        }

        nodesOnTheCurrentLevel.add(startNode);
        ancestors.put(startNode, new HashSet<>());

        int levelY = 0;

        while(!nodesOnTheCurrentLevel.isEmpty()){
            Node currentNode = nodesOnTheCurrentLevel.poll();
            System.out.println("Current node: " + currentNode.getNode());
            if(currentNode == null){
                throw new IllegalArgumentException("Current node is null");
            }
            System.out.println("Current node levelY: " + levelY);
            currentNode.setYLevel(levelY);
            for (ObjectId successorId : currentNode.getNode().getSuccessorNodes()) {
                Node successorNode = idToNode.get(successorId);
                if (successorNode == null) {
                    throw new IllegalArgumentException("Successor node is null");
                }
                System.out.println("Successor node: " + successorNode.getNode());
                if(ancestors.get(currentNode) != null && !ancestors.get(currentNode).contains(successorNode)){
                    System.out.println("Adding successor node to the next level: " + successorNode.getNode());
                    nodesOnTheNextLevel.add(successorNode);
                }
                if(ancestors.get(successorNode) == null){
                    ancestors.put(successorNode, new HashSet<>());
                }
                ancestors.get(successorNode).add(currentNode);
            }
            if(nodesOnTheCurrentLevel.isEmpty()){
                System.out.println("Moving to the next level");
                levelY++;
                System.out.println("Nodes on the next level: " + nodesOnTheNextLevel);
                nodesOnTheCurrentLevel.addAll(nodesOnTheNextLevel);
                //nodesOnTheCurrentLevel = nodesOnTheNextLevel;
                nodesOnTheNextLevel.clear();
                System.out.println("Nodes on the current level: " + nodesOnTheCurrentLevel);
            }
        }
    }

    private void setPositionOfNodes() {

    }

    private double getWidthOfCanvas() {
        // Calculate the width of the canvas
        // The width of the canvas is the maximum x-coordinate of the nodes
        double width = nodes.stream().mapToDouble(Node::getX).max().orElse(0);
        return width + HORIZONTAL_SPACING;
    }

    private double getHeightOfCanvas() {
        // Calculate the height of the canvas
        // The height of the canvas is the maximum y-coordinate of the nodes
        double height = nodes.stream().mapToDouble(Node::getY).max().orElse(0);
        return height + VERTICAL_SPACING;
    }

    private Node createNode(WorkflowNode workflowNode) {
        // Create a Node object based on the type of the WorkflowNode
        switch (workflowNode.getType()) {
            case AND:
                return new AndNode(workflowNode);
            case OR:
                return new OrNode(workflowNode);
            case UNION:
                return new UnionNode(workflowNode);
            case START:
                return new StartNode(workflowNode);
            case END:
                return new EndNode(workflowNode);
            case USER_DECISION:
                return new UserDecisionNode(workflowNode);
            case USER_ACTION:
                return new UserActionNode(workflowNode);
            case BATCH_DECISION:
                return new BatchDecisionNode(workflowNode);
            case BATCH_ACTION:
                return new BatchActionNode(workflowNode);
            default:
                throw new IllegalArgumentException("Invalid node type: " + workflowNode.getType());
        }
    }

    private void drawNodes() {
        for(Node node : this.nodes) {
            this.add(node.getShape());
            this.add(node.getText());
        }
    }

    private void drawConnections() {

    }
}
