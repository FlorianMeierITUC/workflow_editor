package de.ketobi.vaadinspringdemo.entities;

import de.ketobi.vaadinspringdemo.repositories.WorkflowNodeRepository;
import de.ketobi.vaadinspringdemo.repositories.WorkflowRepository;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
public class WorkflowItem {
    private ObjectId workflowId;
    private ArrayList<ObjectId> currentNodesIds = new ArrayList<>();

    public ObjectId getCurrentNode(){
        if(currentNodesIds.size() != 1){
            throw new IllegalStateException("There must be exactly one current node if you call getCurrentNode()");
        }
        return currentNodesIds.get(0);
    }

    public void setCurrentNode(WorkflowNode currentNode){
        currentNodesIds.clear();
        currentNodesIds.add(currentNode.getId());
    }

    public void setCurrentNodes(ArrayList<WorkflowNode> successorNodes) {
        currentNodesIds.clear();
        for(WorkflowNode node : successorNodes){
            currentNodesIds.add(node.getId());
        }
    }

    public void setWorkflow(Workflow workflow) {
        this.workflowId = workflow.getId();
    }
}
