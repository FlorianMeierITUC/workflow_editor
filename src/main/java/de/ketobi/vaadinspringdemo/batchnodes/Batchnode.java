package de.ketobi.vaadinspringdemo.batchnodes;

import org.bson.types.ObjectId;

public interface Batchnode {
    void execute(ObjectId itemId);
}
