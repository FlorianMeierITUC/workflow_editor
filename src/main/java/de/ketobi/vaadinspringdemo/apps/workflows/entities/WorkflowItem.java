package de.ketobi.vaadinspringdemo.apps.workflows.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Component
public abstract class WorkflowItem {
    private ObjectId workflowId;
    private ArrayList<ObjectId> currentNodesIds = new ArrayList<>();
    private ObjectId currentResponsible;
    @Autowired
    private transient MongoTemplate mongoTemplate;

    public void save(){
        saveItem();
    }

    public abstract String getTitle();

    public abstract ObjectId getId();

    public abstract void saveItem();

    protected void saveToDatabase(Object item){
        mongoTemplate.save(item);
    }

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

    public abstract ObjectId getCreatedBy();
    public abstract LocalDateTime getCreatedAt();
}
