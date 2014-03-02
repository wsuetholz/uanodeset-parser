package com.digitalpetri.opcua.nodeset.attributes;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAObject;

public class ObjectNodeAttributes {

    private final BaseNodeAttributes baseNodeAttributes;
    private final UnsignedByte eventNotifier;

    public ObjectNodeAttributes(BaseNodeAttributes baseNodeAttributes, UnsignedByte eventNotifier) {
        this.baseNodeAttributes = baseNodeAttributes;
        this.eventNotifier = eventNotifier;
    }

    public BaseNodeAttributes getBaseNodeAttributes() {
        return baseNodeAttributes;
    }

    public UnsignedByte getEventNotifier() {
        return eventNotifier;
    }

    @Override
    public String toString() {
        return "ObjectNodeAttributes{" +
                "baseNodeAttributes=" + baseNodeAttributes +
                ", eventNotifier=" + eventNotifier +
                '}';
    }

    public static ObjectNodeAttributes fromGenerated(GeneratedUAObject generated) {
        BaseNodeAttributes baseNodeAttributes = BaseNodeAttributes.fromGenerated(generated, NodeClass.Object);

        UnsignedByte eventNotifier = new UnsignedByte(generated.getEventNotifier());

        return new ObjectNodeAttributes(baseNodeAttributes, eventNotifier);
    }
}
