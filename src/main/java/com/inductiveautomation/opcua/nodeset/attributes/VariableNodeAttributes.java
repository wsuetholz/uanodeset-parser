package com.inductiveautomation.opcua.nodeset.attributes;

import javax.xml.bind.Marshaller;
import java.util.Map;
import java.util.Optional;

import com.inductiveautomation.opcua.nodeset.util.AttributeUtil;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UByte;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAVariable;

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

public class VariableNodeAttributes {

    private final NodeAttributes nodeAttributes;

    private final DataValue value;
    private final NodeId dataType;
    private final int valueRank;
    private final Optional<UInteger[]> arrayDimensions;
    private final UByte accessLevel;
    private final UByte userAccessLevel;
    private final Optional<Double> minimumSamplingInterval;
    private final boolean historizing;

    public VariableNodeAttributes(NodeAttributes nodeAttributes,
                                  DataValue value,
                                  NodeId dataType,
                                  int valueRank,
                                  Optional<UInteger[]> arrayDimensions,
                                  UByte accessLevel,
                                  UByte userAccessLevel,
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

    public Optional<UInteger[]> getArrayDimensions() {
        return arrayDimensions;
    }

    public UByte getAccessLevel() {
        return accessLevel;
    }

    public UByte getUserAccessLevel() {
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
        Optional<UInteger[]> arrayDimensions = Optional.of(new UInteger[0]); // TODO gNode.getArrayDimensions();
        UByte accessLevel = ubyte(generated.getAccessLevel());
        UByte userAccessLevel = ubyte(generated.getUserAccessLevel());
        Optional<Double> minimumSamplingInterval = Optional.of(generated.getMinimumSamplingInterval());
        boolean historizing = generated.isHistorizing();

        return new VariableNodeAttributes(nodeAttributes, value, dataType, valueRank, arrayDimensions, accessLevel,
                userAccessLevel, minimumSamplingInterval, historizing);
    }

    private static DataValue value(GeneratedUAVariable.GeneratedValue gValue, Marshaller marshaller) {
        if (gValue == null) return new DataValue(Variant.NullValue);

        return AttributeUtil.parseValue(gValue.getAny(), marshaller);
    }

}
