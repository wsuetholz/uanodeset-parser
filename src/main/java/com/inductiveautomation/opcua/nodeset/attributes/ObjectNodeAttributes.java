package com.inductiveautomation.opcua.nodeset.attributes;

import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAObject;

public class ObjectNodeAttributes {

    private final NodeAttributes nodeAttributes;
    private final short eventNotifier;

    public ObjectNodeAttributes(NodeAttributes nodeAttributes, short eventNotifier) {
        this.nodeAttributes = nodeAttributes;
        this.eventNotifier = eventNotifier;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public short getEventNotifier() {
        return eventNotifier;
    }

    @Override
    public String toString() {
        return "ObjectNodeAttributes{" +
                "nodeAttributes=" + nodeAttributes +
                ", eventNotifier=" + eventNotifier +
                '}';
    }

    public static ObjectNodeAttributes fromGenerated(GeneratedUAObject generated) {
        NodeAttributes nodeAttributes = NodeAttributes.fromGenerated(generated, NodeClass.Object);

        short eventNotifier = generated.getEventNotifier();

        return new ObjectNodeAttributes(nodeAttributes, eventNotifier);
    }

}
