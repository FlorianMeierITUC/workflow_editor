package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.Svg;
import com.vaadin.flow.component.svg.elements.AbstractPolyElement;
import com.vaadin.flow.component.svg.elements.Line;
import com.vaadin.flow.component.svg.elements.Rect;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.entities.WorkflowNodeTypes;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

public class WorkflowView extends Svg {
    private static final int HORIZONTAL_SPACING = 100;
    private static final int VERTICAL_SPACING = 100;
    @Getter
    private final List<Node> nodes = new ArrayList<>();
    private final Map<ObjectId, Node> idToNode = new HashMap<>();
    private int maxYLevel = 0;

    /**
     * The WorkflowView object is a canvas that displays the workflow
     * The nodes are ordered into x and y levels
     * The nodes are drawn on the canvas
     * The connections between the nodes are drawn
     *
     * @param nodes - an unsorted list of WorkflowNode objects
     */
    public WorkflowView(List<WorkflowNode> nodes) {
        super();
        for (WorkflowNode node : nodes) {
            this.nodes.add(createNode(node));
        }
        for (Node node : this.nodes) {
            idToNode.put(node.getNode().getId(), node);
        }
        orderNodesIntoLevels();
        double width = getWidthOfCanvas();
        double height = getHeightOfCanvas();
        viewbox(0, 0, width, height);
        setWidth(width + "px");
        setHeight(height + "px");
        Rect background = new Rect("background", width, height);
        background.setFillColor("white");
        this.add(background);
        drawNodes();
        drawConnections();
    }

    private void orderNodesIntoLevels() {
        Queue<Node> nodesOnTheCurrentLevel = new LinkedList<>();
        Queue<Node> nodesOnTheNextLevel = new LinkedList<>();
        Map<Node, Set<Node>> ancestors = new HashMap<>();

        Node startNode = nodes.stream().filter(n -> n.getNode().getType() == WorkflowNodeTypes.START).findFirst().orElseThrow();
        //check if the start node is present and if it is the only one in the stream
        if (nodes.stream().filter(n -> n.getNode().getType() == WorkflowNodeTypes.START).count() != 1) {
            throw new IllegalArgumentException("There must be exactly one start node in the workflow");
        }
        nodesOnTheCurrentLevel.add(startNode);
        ancestors.put(startNode, new HashSet<>());

        int levelY = 1;

        while (!nodesOnTheCurrentLevel.isEmpty()) {
            Node currentNode = nodesOnTheCurrentLevel.poll();
            if (currentNode == null) {
                throw new IllegalArgumentException("Current node is null");
            }
            if(nodesOnTheCurrentLevel.contains(currentNode)){
                boolean removeDuplicates = true;
                while(removeDuplicates) {
                    removeDuplicates = nodesOnTheCurrentLevel.remove(currentNode);
                }
            }
            currentNode.setYLevel(levelY);
            for (ObjectId successorId : currentNode.getNode().getSuccessorNodes()) {
                Node successorNode = idToNode.get(successorId);
                if (successorNode == null) {
                    throw new IllegalArgumentException("Successor node is null");
                }
                //Cycle detection! If the successor node is already in the ancestors of the current node, skip it
                if (ancestors.get(currentNode) != null && !ancestors.get(currentNode).contains(successorNode)) {
                    nodesOnTheNextLevel.add(successorNode);
                }
                //if the successor node is not in the ancestors map, add it
                ancestors.computeIfAbsent(successorNode, k -> new HashSet<>());
                //add the current node and its ancestors to the ancestors of the successor node
                ancestors.get(successorNode).add(currentNode);
                ancestors.get(successorNode).addAll(ancestors.get(currentNode));
            }
            //if the current level is empty, move to the next level
            if (nodesOnTheCurrentLevel.isEmpty()) {
                levelY++;
                nodesOnTheCurrentLevel.addAll(nodesOnTheNextLevel);
                nodesOnTheNextLevel.clear();
            }
        }
        //Set the maxYLevel and remove 1 because a level gets added after the end node
        maxYLevel = levelY - 1;
        orderNodesIntoXLevels(startNode, ancestors);
    }

    private void orderNodesIntoXLevels(Node startNode, Map<Node, Set<Node>> ancestors){
        //maxXLevel is the amount of nodes on the yLevel with the highest amount of nodes
        int maxXLevel = nodes.stream().mapToInt(Node::getYLevel).max().orElse(0);
        int levelX = 1;
        startNode.setXLevel((maxXLevel+1)/2);
        for (int yLevel = 2; yLevel <= maxYLevel; yLevel++) {
            int finalYLevel = yLevel;
            List<Node> nodesOnThisLevel = nodes.stream().filter(n -> n.getYLevel() == finalYLevel).toList();

            int numberOfClustersOnThisLevel = 1;
            int relativeX = 0;
            for (Node node : nodesOnThisLevel) {
                if(yLevel == 2){
                    node.setXLevel(levelX);
                }
                // Count the successors of the current node
                List<Node> successors = node.getNode().getSuccessorNodes().stream().map(idToNode::get).toList();
                List<Node> successorsOnThisLevel = successors.stream().filter(n -> n.getYLevel() == finalYLevel + 1).toList();
                if (numberOfClustersOnThisLevel>1){
                    relativeX += 2;
                }
                for (Node successor : successorsOnThisLevel) {
                    successor.setXLevel(node.getXLevel() + relativeX);
                    relativeX+=2;
                    if(successor.getNode().getType() == WorkflowNodeTypes.UNION){
                        int tempX = 0;
                        for (Node tempNode : nodesOnThisLevel){
                            tempX += tempNode.getXLevel();
                        }
                        successor.setXLevel(tempX/nodesOnThisLevel.size());
                    }
                    System.out.println("Successor node: " + successor.getNode().getTitle() + " x: " + successor.getXLevel() + " y: " + successor.getYLevel());
                }
                node.setXLevel((int) Math.round(successorsOnThisLevel.stream().mapToDouble(Node::getXLevel).average().orElse(0)));
                for(Node ancestor : ancestors.get(node)){
                    //ancestor.setXLevel(node.getXLevel());
                }
                System.out.println("Node: " + node.getNode().getTitle() + " x: " + node.getXLevel() + " y: " + node.getYLevel());
                System.out.println("-----------");
                numberOfClustersOnThisLevel++;
                relativeX = 0;
            }
        }
        Node endNode = nodes.stream().filter(n -> n.getNode().getType() == WorkflowNodeTypes.END).findFirst().orElseThrow();
        //check if the end node is present and if it is the only one in the stream
        if (nodes.stream().filter(n -> n.getNode().getType() == WorkflowNodeTypes.END).count() != 1) {
            throw new IllegalArgumentException("There must be exactly one start node in the workflow");
        }
        endNode.setXLevel((maxXLevel+1)/2);
    }

    /**
     * Calculate the width of the canvas
     * The width of the canvas is the maximum x-coordinate of the nodes
     *
     * @return the width of the canvas
     */
    private double getWidthOfCanvas() {
        double width = nodes.stream().mapToDouble(Node::getXLevel).max().orElse(0) * HORIZONTAL_SPACING + 2 * HORIZONTAL_SPACING;
        return width;
    }

    /**
     * Calculate the height of the canvas
     * The height of the canvas is the maximum y-coordinate of the nodes
     *
     * @return the height of the canvas
     */
    private double getHeightOfCanvas() {
        double height = nodes.stream().mapToDouble(Node::getYLevel).max().orElse(0) * VERTICAL_SPACING + 2 * VERTICAL_SPACING;
        return height;
    }

    /**
     * Create a Node object based on the type of the WorkflowNode
     *
     * @param workflowNode the WorkflowNode object
     * @return the Node object that extends SVGElement
     */
    private Node createNode(WorkflowNode workflowNode) {
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

    /**
     * Draw nodes on the canvas according to their x and y levels
     */
    private void drawNodes() {
        for (Node node : this.nodes) {
            node.move(node.getXLevel() * HORIZONTAL_SPACING, node.getYLevel() * VERTICAL_SPACING);
            this.add(node.getShape());
            this.add(node.getText());
        }
    }

    /**
     * Draw connections between nodes
     * A line is drawn between the bottom connector of a node and the top connector of its successor
     */
    private void drawConnections() {
        for (Node node : this.nodes) {
            for (ObjectId successorId : node.getNode().getSuccessorNodes()) {
                Node successorNode = idToNode.get(successorId);
                if (successorNode == null) {
                    throw new IllegalArgumentException("Successor node is null");
                }
                AbstractPolyElement.PolyCoordinatePair start = node.getBottomConnector();
                AbstractPolyElement.PolyCoordinatePair end = successorNode.getTopConnector();
                Line line = new Line(node.getNode().getTitle() + " to " + successorNode.getNode().getTitle(), start, end);
                if (node.getNode().getType().equals(WorkflowNodeTypes.BATCH_DECISION) || node.getNode().getType().equals(WorkflowNodeTypes.USER_DECISION)) {
                    if (successorNode.getNode().getId().equals(node.getNode().getSuccessorNode_success())) {
                        line.setStroke("green", 2);
                    } else if (successorNode.getNode().getId().equals(node.getNode().getSuccessorNode_failure())) {
                        line.setStroke("red", 2);
                    } else {
                        throw new IllegalArgumentException("Failed to draw a line from " + node.getNode().getTitle() + " to the successor nodes because a node that is neither success or failure was found!");
                    }
                } else {
                    line.setStroke("black", 2);
                }
                this.add(line);
            }
        }
    }
}
