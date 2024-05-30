package de.ketobi.vaadinspringdemo.main.workflows.batchnodes;

import org.bson.types.ObjectId;

public interface Batchnode {
    void execute(ObjectId itemId);
}
