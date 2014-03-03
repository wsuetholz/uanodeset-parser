package com.digitalpetri.opcua.nodeset.attributes;

import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAObjectType;

public class ObjectTypeNodeAttributes {

    private final NodeAttributes nodeAttributes;
    private final boolean isAbstract;

    public ObjectTypeNodeAttributes(NodeAttributes nodeAttributes, boolean isAbstract) {
        this.nodeAttributes = nodeAttributes;
        this.isAbstract = isAbstract;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public String toString() {
        return "ObjectTypeNodeAttributes{" +
                "nodeAttributes=" + nodeAttributes +
                ", isAbstract=" + isAbstract +
                '}';
    }

    public static ObjectTypeNodeAttributes fromGenerated(GeneratedUAObjectType generated) {
        NodeAttributes nodeAttributes = NodeAttributes.fromGenerated(generated, NodeClass.ObjectType);
        boolean isAbstract = generated.isIsAbstract();

        return new ObjectTypeNodeAttributes(nodeAttributes, isAbstract);
    }

}
