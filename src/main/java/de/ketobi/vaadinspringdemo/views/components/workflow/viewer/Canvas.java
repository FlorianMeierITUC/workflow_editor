package de.ketobi.vaadinspringdemo.views.components.workflow.viewer;

import com.vaadin.flow.component.svg.Svg;
import com.vaadin.flow.component.svg.elements.Line;
import com.vaadin.flow.component.svg.elements.Rect;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.entities.WorkflowNodeTypes;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

public class Canvas extends Svg {
    private static final int HORIZONTAL_SPACING = 150;
    private static final int VERTICAL_SPACING = 100;
    //All nodes in the workflow
    //private List<WorkflowNode> nodes;
    private List<Node> nodes;
    //Map of the node id to the Node object
    private Map<String, Node> nodeMap = new HashMap<>();
    //Map of the node id to the WorkflowNode object
    private Map<ObjectId, WorkflowNode> workflowNodeMap = new HashMap<>();
    private Set<ObjectId> drawnNodes = new HashSet<>();
    private Map<ObjectId, Integer> parentCountMap = new HashMap<>();

    public Canvas(List<WorkflowNode> nodes) {
        super();
        for(WorkflowNode node : nodes) {
            this.nodes.add(createNode(node));
        }
        viewbox(0, 0, 1800, 1800);
        setWidth("100%");
        setHeight("1500px");
        Rect background = new Rect("background", 1800, 1800);
        background.setFillColor("white");
        this.add(background);
        WorkflowNode startNode = nodes.stream().filter(n -> n.getType() == WorkflowNodeTypes.START).findFirst().orElseThrow();
        for(WorkflowNode node : nodes) {
            workflowNodeMap.put(node.getId(), node);
            if(null != node.getPredecessorNodes()) {
                parentCountMap.put(node.getId(), node.getPredecessorNodes().size());
            }
        }
        int currentLevel = 1;
        Queue<WorkflowNode> queue = new LinkedList<>();
        queue.add(startNode);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            int currentX = HORIZONTAL_SPACING;
            for (int i = 0; i < levelSize; i++) {
                WorkflowNode node = queue.poll();
                System.out.println("Processing Node: " + node);
                Node svgNode;
                if (!drawnNodes.contains(node.getId())) {
                    svgNode = createNode(node);
                    System.out.println("Draw Node and put to map: " + node + " "+ svgNode.getId() + " "  + currentX + " " + currentLevel * VERTICAL_SPACING);
                    nodeMap.put(svgNode.getId(), svgNode);
                    this.add(svgNode.getShape());
                    this.add(svgNode.getText());
                    drawnNodes.add(node.getId());
                } else {
                    svgNode = nodeMap.get(node.getId().toString());
                    System.out.println("Move Node: " + node + " " + currentX + " " + currentLevel * VERTICAL_SPACING);
                    svgNode.move(currentX, currentLevel * VERTICAL_SPACING);
                }

                if (node.getPredecessorNodes() != null && !node.getPredecessorNodes().isEmpty()) {
                    System.out.println("Iterate over parent nodes for node: " + node);
                    for (ObjectId predecessorId : node.getPredecessorNodes()) {
                        Node parentNode = nodeMap.get(predecessorId.toString());
                        if (parentNode == null) {
                            throw new IllegalArgumentException("Parent node not found in the node map: " + predecessorId);
                        }
                        System.out.println("Current Node: " + node);
                        Line line = new Line("line", parentNode.getBottomConnector(), svgNode.getTopConnector());
                        line.setStroke("black", 2);
                        System.out.println("Draw Line from Parent Node: " + parentNode.getId() + " to " + svgNode.getId());
                        this.add(line);
                    }
                }

                if(node.getSuccessorNodes() != null && !node.getSuccessorNodes().isEmpty()) {
                    for (ObjectId child : node.getSuccessorNodes()) {
                        System.out.println("Found child Node: " + child);
                        parentCountMap.put(child, parentCountMap.get(child) - 1);
                        if (parentCountMap.get(child) == 0) {
                            queue.add(workflowNodeMap.get(child));
                            System.out.println("Added child Node: " + workflowNodeMap.get(child).getTitle() + " to queue");
                        }
                    }
                }
                currentX += HORIZONTAL_SPACING;
            }

            currentLevel++;
        }
    }
/*
    private void createNodePositionsInTree(){
        int yLevel = 0;
        int xLevel = 0;
        //Create a map of WorkflowNode to 2D array
        //Key: WorkflowNode, Value: Array with the x and y position
        Map<WorkflowNode, int[]> nodePositions = new HashMap<>();
        //Get the start node
        WorkflowNode startNode = nodes.stream().filter(n -> n.getType() == WorkflowNodeTypes.START).findFirst().orElseThrow();
        //Check if the filtered stream has more than one element
        if(nodes.stream().filter(n -> n.getType() == WorkflowNodeTypes.START).count() > 1){
            throw new IllegalArgumentException("More than one start node found");
        }
        WorkflowNode currentNode = startNode;
        //Iterate over the successors
        ArrayList<WorkflowNode> successors = new ArrayList<>();
        for(ObjectId successorId : currentNode.getSuccessorNodes()){
            successors.add(workflowNodeMap.get(successorId));
        }

        //put the start node in the map with the x and y position
        nodePositions.put(startNode, new int[]{xLevel, yLevel});

        //Find the longest way from start node to the end node using only the successor nodes with a recursive algorithm
        int longestPathLength = findLongestPathLength(startNode);




    }
*/
    public int findLongestPathLength(Node startNode) {
        Map<Node, List<Node>> adjacencyList = new HashMap<>();
        for (Node node : nodes) {
            adjacencyList.put(node, node.getNode().getSuccessorNodes().stream()
                    .map(successorId -> nodes.stream()
                            .filter(n -> n.getId().equals(successorId))
                            .findFirst()
                            .orElseThrow())
                    .collect(Collectors.toList()));
        }

        return dfs(startNode, adjacencyList, 0);
    }

    private int dfs(Node node, Map<Node, List<Node>> adjacencyList, int depth) {
        int maxDepth = depth;
        for (Node successorNode : adjacencyList.get(node)) {
            maxDepth = Math.max(maxDepth, dfs(successorNode, adjacencyList, depth + 1));
        }
        return maxDepth;
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
}