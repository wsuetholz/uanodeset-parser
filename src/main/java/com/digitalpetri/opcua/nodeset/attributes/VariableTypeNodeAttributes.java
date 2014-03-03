package com.digitalpetri.opcua.nodeset.attributes;

import javax.xml.bind.Marshaller;
import java.util.Map;
import java.util.Optional;

import com.digitalpetri.opcua.nodeset.util.AttributeUtil;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAVariableType;

public class VariableTypeNodeAttributes {

    private final NodeAttributes nodeAttributes;

    private final Optional<DataValue> value;
    private final NodeId dataType;
    private final int valueRank;
    private final Optional<UnsignedInteger[]> arrayDimensions;
    private final boolean isAbstract;

    public VariableTypeNodeAttributes(NodeAttributes nodeAttributes,
                                      Optional<DataValue> value,
                                      NodeId dataType,
                                      int valueRank,
                                      Optional<UnsignedInteger[]> arrayDimensions,
                                      boolean isAbstract) {

        this.nodeAttributes = nodeAttributes;
        this.value = value;
        this.dataType = dataType;
        this.valueRank = valueRank;
        this.arrayDimensions = arrayDimensions;
        this.isAbstract = isAbstract;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public Optional<DataValue> getValue() {
        return value;
    }

    public NodeId getDataType() {
        return dataType;
    }

    public int getValueRank() {
        return valueRank;
    }

    public Optional<UnsignedInteger[]> getArrayDimensions() {
        return arrayDimensions;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public String toString() {
        return "VariableTypeNodeAttributes{" +
                "nodeAttributes=" + nodeAttributes +
                ", value=" + value +
                ", dataType=" + dataType +
                ", valueRank=" + valueRank +
                ", arrayDimensions=" + arrayDimensions +
                ", isAbstract=" + isAbstract +
                '}';
    }

    public static VariableTypeNodeAttributes fromGenerated(GeneratedUAVariableType generated,
                                                           Marshaller marshaller,
                                                           Map<String, NodeId> aliasMap) {

        NodeAttributes nodeAttributes = NodeAttributes.fromGenerated(generated, NodeClass.VariableType);

        Optional<DataValue> value = value(generated.getValue(), marshaller);
        NodeId dataType = AttributeUtil.parseDataType(generated.getDataType(), aliasMap);
        int valueRank = generated.getValueRank();
        Optional<UnsignedInteger[]> arrayDimensions = Optional.of(new UnsignedInteger[0]); // TODO gNode.getArrayDimensions();
        boolean isAbstract = generated.isIsAbstract();

        return new VariableTypeNodeAttributes(nodeAttributes, value, dataType, valueRank, arrayDimensions, isAbstract);
    }

    private static Optional<DataValue> value(GeneratedUAVariableType.GeneratedValue gValue, Marshaller marshaller) {
        if (gValue == null) return Optional.empty();

        return Optional.of(AttributeUtil.parseValue(gValue.getAny(), marshaller));
    }

}
