package com.digitalpetri.opcua.nodeset.attributes;

import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUADataType;

public class DataTypeNodeAttributes {

    private final BaseNodeAttributes baseNodeAttributes;
    private final boolean isAbstract;

    public DataTypeNodeAttributes(BaseNodeAttributes baseNodeAttributes, boolean isAbstract) {
        this.baseNodeAttributes = baseNodeAttributes;
        this.isAbstract = isAbstract;
    }

    public BaseNodeAttributes getBaseNodeAttributes() {
        return baseNodeAttributes;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public String toString() {
        return "DataTypeNodeAttributes{" +
                "baseNodeAttributes=" + baseNodeAttributes +
                ", isAbstract=" + isAbstract +
                '}';
    }

    public static DataTypeNodeAttributes fromGenerated(GeneratedUADataType generated) {
        BaseNodeAttributes baseNodeAttributes = BaseNodeAttributes.fromGenerated(generated, NodeClass.DataType);
        boolean isAbstract = generated.isIsAbstract();

        return new DataTypeNodeAttributes(baseNodeAttributes, isAbstract);
    }

}
