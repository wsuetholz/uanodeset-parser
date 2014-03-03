package com.digitalpetri.opcua.nodeset.attributes;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAObject;

public class ObjectNodeAttributes {

    private final NodeAttributes nodeAttributes;
    private final UnsignedByte eventNotifier;

    public ObjectNodeAttributes(NodeAttributes nodeAttributes, UnsignedByte eventNotifier) {
        this.nodeAttributes = nodeAttributes;
        this.eventNotifier = eventNotifier;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public UnsignedByte getEventNotifier() {
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

        UnsignedByte eventNotifier = new UnsignedByte(generated.getEventNotifier());

        return new ObjectNodeAttributes(nodeAttributes, eventNotifier);
    }
}
