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
    // Create a map of the node id to the Node object
    private Map<ObjectId, Node> idToNode = new HashMap<>();
    private static final int HORIZONTAL_SPACING = 100;
    private static final int VERTICAL_SPACING = 100;
    private int maxYLevel = 0;

    public WorkflowView(List<WorkflowNode> nodes) {
        super();
        for(WorkflowNode node : nodes) {
            this.nodes.add(createNode(node));
        }
        for(Node node : this.nodes) {
            idToNode.put(node.getNode().getId(), node);
        }
        orderNodesIntoYLevels();
        orderNodesIntoXLevels();
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
        Map<Node, Set<Node>> ancestors = new HashMap<>();

        Node startNode = nodes.stream().filter(n -> n.getNode().getType() == WorkflowNodeTypes.START).findFirst().orElseThrow();
        //check if the start node is present and if it is the only one in the stream
        if (nodes.stream().filter(n -> n.getNode().getType() == WorkflowNodeTypes.START).count() != 1) {
            throw new IllegalArgumentException("There must be exactly one start node in the workflow");
        }

        nodesOnTheCurrentLevel.add(startNode);
        ancestors.put(startNode, new HashSet<>());

        int levelY = 1;

        while(!nodesOnTheCurrentLevel.isEmpty()){
            Node currentNode = nodesOnTheCurrentLevel.poll();
            if(currentNode == null){
                throw new IllegalArgumentException("Current node is null");
            }
            currentNode.setYLevel(levelY);
            for (ObjectId successorId : currentNode.getNode().getSuccessorNodes()) {
                Node successorNode = idToNode.get(successorId);
                if (successorNode == null) {
                    throw new IllegalArgumentException("Successor node is null");
                }
                if(ancestors.get(currentNode) != null && !ancestors.get(currentNode).contains(successorNode)){
                    nodesOnTheNextLevel.add(successorNode);
                }
                if(ancestors.get(successorNode) == null){
                    ancestors.put(successorNode, new HashSet<>());
                }
                ancestors.get(successorNode).add(currentNode);
                ancestors.get(successorNode).addAll(ancestors.get(currentNode));
            }
            if(nodesOnTheCurrentLevel.isEmpty()){
                levelY++;
                nodesOnTheCurrentLevel.addAll(nodesOnTheNextLevel);
                nodesOnTheNextLevel.clear();
            }
        }
        //Set the maxYLevel and remove 1 because a level gets added after the end node
        maxYLevel = levelY - 1;
    }

    private void orderNodesIntoXLevels() {
        System.out.println("Max Y level: "+maxYLevel);
        for (int yLevel = 1; yLevel <= maxYLevel; yLevel++) {
            int finalYLevel = yLevel;
            List<Node> nodesOnThisLevel = nodes.stream().filter(n -> n.getYLevel() == finalYLevel).toList();
            int totalAmountOfNodesOnThisLevel = nodesOnThisLevel.size();
            System.out.println("Amount of nodes on level "+yLevel+": "+totalAmountOfNodesOnThisLevel);
            int amountOfSuccessors = 0;
            int xLevel = 1;
            for (Node node : nodesOnThisLevel) {
                List<Node> successors = node.getNode().getSuccessorNodes().stream().map(idToNode::get).toList();
                amountOfSuccessors = successors.size();
                System.out.println("Amount of successors: "+amountOfSuccessors);
                node.setXLevel(xLevel);
                System.out.println("Node "+node.getNode().getTitle()+" is on x level "+xLevel);
                xLevel++;
            }
        }
    }

    private double getWidthOfCanvas() {
        // Calculate the width of the canvas
        // The width of the canvas is the maximum x-coordinate of the nodes
        double width = nodes.stream().mapToDouble(Node::getXLevel).max().orElse(0) * HORIZONTAL_SPACING + 2*HORIZONTAL_SPACING;
        System.out.println("Width: "+width);
        return width;
    }

    private double getHeightOfCanvas() {
        // Calculate the height of the canvas
        // The height of the canvas is the maximum y-coordinate of the nodes
        double height = nodes.stream().mapToDouble(Node::getYLevel).max().orElse(0) * VERTICAL_SPACING + 2*VERTICAL_SPACING;
        System.out.println("Height: "+height);
        return height;
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
            node.move(node.getXLevel() * HORIZONTAL_SPACING, node.getYLevel() * VERTICAL_SPACING);
            this.add(node.getShape());
            this.add(node.getText());
        }
    }

    private void drawConnections() {

    }
}
