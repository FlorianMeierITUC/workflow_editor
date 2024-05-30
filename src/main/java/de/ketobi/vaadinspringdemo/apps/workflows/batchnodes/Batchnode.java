package de.ketobi.vaadinspringdemo.apps.workflows.batchnodes;

import org.bson.types.ObjectId;

public interface Batchnode {
    void execute(ObjectId itemId);
}
