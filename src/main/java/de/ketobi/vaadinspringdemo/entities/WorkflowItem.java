package de.ketobi.vaadinspringdemo.entities;

import lombok.Data;

import java.util.ArrayList;

@Data
public class WorkflowItem {
    private Workflow  workflow;
    private ArrayList<WorkflowNode> currentNodes = new ArrayList<>();

    public WorkflowNode getCurrentNode(){
        if(currentNodes.size() != 1){
            throw new IllegalStateException("There must be exactly one current node if you call getCurrentNode()");
        }
        return currentNodes.get(0);
    }

    public void setCurrentNode(WorkflowNode currentNode){
        currentNodes.clear();
        currentNodes.add(currentNode);
    }
}
