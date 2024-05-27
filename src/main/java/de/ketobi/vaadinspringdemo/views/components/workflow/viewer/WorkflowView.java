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
    private Node highlightNode;

    /**
     * The WorkflowView object is a canvas that displays the workflow
     * The nodes are ordered into x and y levels
     * The nodes are drawn on the canvas
     * The connections between the nodes are drawn
     *
     * @param nodes - an unsorted list of WorkflowNode objects
     */
    public WorkflowView(List<WorkflowNode> nodes) {
        this(nodes, null);
    }

    public WorkflowView(List<WorkflowNode> nodes, WorkflowNode highlightNode) {
        super();
        this.highlightNode = createNode(highlightNode);
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
            if (nodesOnTheCurrentLevel.contains(currentNode)) {
                boolean removeDuplicates = true;
                while (removeDuplicates) {
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

    private void orderNodesIntoXLevels(Node startNode, Map<Node, Set<Node>> ancestors) {
        // Initialize a map to store nodes grouped by their Y level
        Map<Integer, List<Node>> nodesByYLevel = new HashMap<>();

        for (Node node : nodes) {
            nodesByYLevel.computeIfAbsent(node.getYLevel(), k -> new ArrayList<>()).add(node);
        }

        // Initial X level assignment based on successors
        for (int yLevel = maxYLevel; yLevel >= 1; yLevel--) {
            List<Node> nodesOnThisYLevel = nodesByYLevel.getOrDefault(yLevel, new ArrayList<>());

            if (nodesOnThisYLevel.isEmpty()) {
                continue;
            }

            for (Node node : nodesOnThisYLevel) {
                List<Integer> successorXLevels = node.getNode().getSuccessorNodes().stream().map(idToNode::get).map(Node::getXLevel).collect(Collectors.toList());

                if (!successorXLevels.isEmpty()) {
                    int averageXLevel = (int) successorXLevels.stream().mapToInt(Integer::intValue).average().orElse(node.getXLevel());
                    node.setXLevel(averageXLevel);
                }
            }
        }

        // Adjust X levels based on predecessors
        for (int yLevel = 1; yLevel <= maxYLevel; yLevel++) {
            List<Node> nodesOnThisYLevel = nodesByYLevel.getOrDefault(yLevel, new ArrayList<>());

            if (nodesOnThisYLevel.isEmpty()) {
                continue;
            }

            for (Node node : nodesOnThisYLevel) {
                List<Integer> predecessorXLevels = node.getNode().getPredecessorNodes().stream().map(idToNode::get).map(Node::getXLevel).collect(Collectors.toList());

                if (!predecessorXLevels.isEmpty()) {
                    int averageXLevel = (int) predecessorXLevels.stream().mapToInt(Integer::intValue).average().orElse(node.getXLevel());
                    node.setXLevel(averageXLevel);
                }
            }
        }

        // Final adjustment to ensure proper spacing and avoid overlaps
        for (int yLevel = 1; yLevel <= maxYLevel; yLevel++) {
            List<Node> nodesOnThisYLevel = nodesByYLevel.getOrDefault(yLevel, new ArrayList<>());

            nodesOnThisYLevel.sort(Comparator.comparingInt(Node::getXLevel));

            for (int xLevel = 0; xLevel < nodesOnThisYLevel.size(); xLevel++) {
                nodesOnThisYLevel.get(xLevel).setXLevel((xLevel + 1) * 2); // Double the X level to add extra space
            }
        }

        //Shift the nodes to the right to center them on the canvas and above their successors
        //Set the start node to the center of the yLevel with the most nodes
        int maxNodesOnYLevel = nodesByYLevel.values().stream().mapToInt(List::size).max().orElse(0);
        int xShift = maxNodesOnYLevel - 1;

        for (int yLevel = 1; yLevel <= maxYLevel; yLevel++) {
            final int finalYLevel = yLevel;
            List<Node> nodesOnThisYLevel = nodesByYLevel.getOrDefault(yLevel, new ArrayList<>());

            if (nodesOnThisYLevel.size() == 1) {
                Node node = nodesOnThisYLevel.get(0);
                switch (node.getNode().getType()) {
                    case OR:
                    case AND:
                        int amountOfSuccessorsOnTheNextYLevel = node.getNode().getSuccessorNodes().stream().map(idToNode::get).filter(n -> n.getYLevel() == finalYLevel + 1).mapToInt(n -> 1).sum();
                        // Center the node above its successors on the next y level
                        int centerAboveSuccessors = node.getNode().getSuccessorNodes().stream().map(idToNode::get).filter(n -> n.getYLevel() == finalYLevel + 1).mapToInt(Node::getXLevel).sum() / amountOfSuccessorsOnTheNextYLevel;
                        node.setXLevel(centerAboveSuccessors);
                        break;
                    case UNION:
                        //center the union node under its predecessors on the previous y level
                        int centerUnderPredecessors = node.getNode().getPredecessorNodes().stream().map(idToNode::get).filter(n -> n.getYLevel() == finalYLevel - 1).mapToInt(Node::getXLevel).sum() / node.getNode().getPredecessorNodes().size();
                        node.setXLevel(centerUnderPredecessors);
                        break;
                    default:
                        int predecessorsOnThePreviousLevel = node.getNode().getPredecessorNodes().stream().map(idToNode::get).filter(n -> n.getYLevel() == finalYLevel - 1).toList().size();
                        if (predecessorsOnThePreviousLevel == 1) {
                            Node predecessor = idToNode.get(node.getNode().getPredecessorNodes().get(0));
                            if ((predecessor.getNode().getType().equals(WorkflowNodeTypes.USER_DECISION) || predecessor.getNode().getType().equals(WorkflowNodeTypes.BATCH_DECISION)) && predecessor.getYLevel() == yLevel - 1) {
                                if (predecessor.getNode().getSuccessorNode_success().equals(node.getNode().getId())) {
                                    node.setXLevel(xShift + node.getXLevel() - 1);
                                } else if (predecessor.getNode().getSuccessorNode_failure().equals(node.getNode().getId())) {
                                    node.setXLevel(xShift + node.getXLevel() + 1);
                                } else {
                                    node.setXLevel(xShift + node.getXLevel());
                                }
                            } else {
                                node.setXLevel(xShift + node.getXLevel());
                            }
                        } else {
                            node.setXLevel(xShift + node.getXLevel());
                        }
                        break;
                }

            } else {
                for (Node node : nodesOnThisYLevel) {
                    switch (node.getNode().getType()) {
                        case OR:
                        case AND:
                            int amountOfSuccessorsOnTheNextYLevel = node.getNode().getSuccessorNodes().stream().map(idToNode::get).filter(n -> n.getYLevel() == finalYLevel + 1).mapToInt(n -> 1).sum();
                            if(amountOfSuccessorsOnTheNextYLevel <= 1){
                                break;
                            }
                            // Center the node above its successors on the next y level
                            int centerAboveSuccessors = node.getNode().getSuccessorNodes().stream().map(idToNode::get).filter(n -> n.getYLevel() == finalYLevel + 1).mapToInt(Node::getXLevel).sum() / amountOfSuccessorsOnTheNextYLevel;
                            node.setXLevel(centerAboveSuccessors);
                            break;
                        case UNION:
                            //center the union node under its predecessors on the previous y level
                            int centerUnderPredecessors = node.getNode().getPredecessorNodes().stream().map(idToNode::get).filter(n -> n.getYLevel() == finalYLevel - 1).mapToInt(Node::getXLevel).sum() / node.getNode().getPredecessorNodes().size();
                            node.setXLevel(centerUnderPredecessors);
                            break;
                        default:
                            break;
                    }
                }
            }

        }
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
            System.out.println("Highlight node: "+highlightNode);
            if (highlightNode != null) {
                System.out.println("Highlight node ID: "+highlightNode.getNode().getId());
            }
            if (highlightNode != null && node.getNode().getId().equals(highlightNode.getNode().getId())) {
                node.getShape().setFillColor("yellow");
            }
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
