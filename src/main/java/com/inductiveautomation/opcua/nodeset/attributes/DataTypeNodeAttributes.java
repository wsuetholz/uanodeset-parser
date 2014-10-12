package com.inductiveautomation.opcua.nodeset.attributes;

import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUADataType;

public class DataTypeNodeAttributes {

    private final NodeAttributes nodeAttributes;
    private final boolean isAbstract;

    public DataTypeNodeAttributes(NodeAttributes nodeAttributes, boolean isAbstract) {
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
        return "DataTypeNodeAttributes{" +
                "nodeAttributes=" + nodeAttributes +
                ", isAbstract=" + isAbstract +
                '}';
    }

    public static DataTypeNodeAttributes fromGenerated(GeneratedUADataType generated) {
        NodeAttributes nodeAttributes = NodeAttributes.fromGenerated(generated, NodeClass.DataType);
        boolean isAbstract = generated.isIsAbstract();

        return new DataTypeNodeAttributes(nodeAttributes, isAbstract);
    }

}
