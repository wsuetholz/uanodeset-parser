package com.digitalpetri.opcua.nodeset.attributes;

import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAObjectType;

public class ObjectTypeNodeAttributes {

    private final BaseNodeAttributes baseNodeAttributes;
    private final boolean isAbstract;

    public ObjectTypeNodeAttributes(BaseNodeAttributes baseNodeAttributes, boolean isAbstract) {
        this.baseNodeAttributes = baseNodeAttributes;
        this.isAbstract = isAbstract;
    }

    public BaseNodeAttributes getBaseNodeAttributes() {
        return baseNodeAttributes;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public static ObjectTypeNodeAttributes fromGenerated(GeneratedUAObjectType generated) {
        BaseNodeAttributes baseNodeAttributes = BaseNodeAttributes.fromGenerated(generated, NodeClass.ObjectType);
        boolean isAbstract = generated.isIsAbstract();

        return new ObjectTypeNodeAttributes(baseNodeAttributes, isAbstract);
    }

}
