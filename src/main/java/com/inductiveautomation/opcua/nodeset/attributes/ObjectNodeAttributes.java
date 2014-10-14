package com.inductiveautomation.opcua.nodeset.attributes;

import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UByte;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAObject;

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

public class ObjectNodeAttributes {

    private final NodeAttributes nodeAttributes;
    private final UByte eventNotifier;

    public ObjectNodeAttributes(NodeAttributes nodeAttributes, UByte eventNotifier) {
        this.nodeAttributes = nodeAttributes;
        this.eventNotifier = eventNotifier;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public UByte getEventNotifier() {
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

        UByte eventNotifier = ubyte(generated.getEventNotifier());

        return new ObjectNodeAttributes(nodeAttributes, eventNotifier);
    }

}
