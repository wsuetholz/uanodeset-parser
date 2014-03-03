package com.digitalpetri.opcua.nodeset.attributes;

import javax.xml.bind.Marshaller;
import java.util.Map;
import java.util.Optional;

import com.digitalpetri.opcua.nodeset.util.AttributeUtil;
import org.opcfoundation.ua.builtintypes.*;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAVariable;

public class VariableNodeAttributes {

    private final NodeAttributes nodeAttributes;

    private final DataValue value;
    private final NodeId dataType;
    private final int valueRank;
    private final Optional<UnsignedInteger[]> arrayDimensions;
    private final UnsignedByte accessLevel;
    private final UnsignedByte userAccessLevel;
    private final Optional<Double> minimumSamplingInterval;
    private final boolean historizing;

    public VariableNodeAttributes(NodeAttributes nodeAttributes,
                                  DataValue value,
                                  NodeId dataType,
                                  int valueRank,
                                  Optional<UnsignedInteger[]> arrayDimensions,
                                  UnsignedByte accessLevel,
                                  UnsignedByte userAccessLevel,
                                  Optional<Double> minimumSamplingInterval,
                                  boolean historizing) {

        this.nodeAttributes = nodeAttributes;
        this.value = value;
        this.dataType = dataType;
        this.valueRank = valueRank;
        this.arrayDimensions = arrayDimensions;
        this.accessLevel = accessLevel;
        this.userAccessLevel = userAccessLevel;
        this.minimumSamplingInterval = minimumSamplingInterval;
        this.historizing = historizing;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public DataValue getValue() {
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

    public UnsignedByte getAccessLevel() {
        return accessLevel;
    }

    public UnsignedByte getUserAccessLevel() {
        return userAccessLevel;
    }

    public Optional<Double> getMinimumSamplingInterval() {
        return minimumSamplingInterval;
    }

    public boolean isHistorizing() {
        return historizing;
    }

    @Override
    public String toString() {
        return "VariableNodeAttributes{" +
                "nodeAttributes=" + nodeAttributes +
                ", value=" + value +
                ", dataType=" + dataType +
                ", valueRank=" + valueRank +
                ", arrayDimensions=" + arrayDimensions +
                ", accessLevel=" + accessLevel +
                ", userAccessLevel=" + userAccessLevel +
                ", minimumSamplingInterval=" + minimumSamplingInterval +
                ", historizing=" + historizing +
                '}';
    }

    public static VariableNodeAttributes fromGenerated(GeneratedUAVariable generated,
                                                       Marshaller marshaller,
                                                       Map<String, NodeId> aliasMap) {

        NodeAttributes nodeAttributes = NodeAttributes.fromGenerated(generated, NodeClass.Variable);

        DataValue value = value(generated.getValue(), marshaller);
        NodeId dataType = AttributeUtil.parseDataType(generated.getDataType(), aliasMap);
        int valueRank = generated.getValueRank();
        Optional<UnsignedInteger[]> arrayDimensions = Optional.of(new UnsignedInteger[0]); // TODO gNode.getArrayDimensions();
        UnsignedByte accessLevel = new UnsignedByte(generated.getAccessLevel());
        UnsignedByte userAccessLevel = new UnsignedByte(generated.getUserAccessLevel());
        Optional<Double> minimumSamplingInterval = Optional.of(generated.getMinimumSamplingInterval());
        boolean historizing = generated.isHistorizing();

        return new VariableNodeAttributes(nodeAttributes, value, dataType, valueRank, arrayDimensions, accessLevel,
                userAccessLevel, minimumSamplingInterval, historizing);
    }

    private static DataValue value(GeneratedUAVariable.GeneratedValue gValue, Marshaller marshaller) {
        if (gValue == null) return new DataValue(Variant.NULL);

        return AttributeUtil.parseValue(gValue.getAny(), marshaller);
    }

}
