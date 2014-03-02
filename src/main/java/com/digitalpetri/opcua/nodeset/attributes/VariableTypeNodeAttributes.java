package com.digitalpetri.opcua.nodeset.attributes;

import javax.xml.bind.Marshaller;
import java.util.Optional;

import com.digitalpetri.opcua.nodeset.UaNodeSetParser;
import com.digitalpetri.opcua.nodeset.util.AttributeUtil;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAVariable;
import org.opcfoundation.ua.generated.GeneratedUAVariableType;

public class VariableTypeNodeAttributes {

    private final BaseNodeAttributes baseNodeAttributes;

    private final Optional<DataValue> value;
    private final NodeId dataType;
    private final int valueRank;
    private final Optional<UnsignedInteger[]> arrayDimensions;
    private final boolean isAbstract;

    public VariableTypeNodeAttributes(BaseNodeAttributes baseNodeAttributes,
                                      Optional<DataValue> value,
                                      NodeId dataType,
                                      int valueRank,
                                      Optional<UnsignedInteger[]> arrayDimensions,
                                      boolean isAbstract) {

        this.baseNodeAttributes = baseNodeAttributes;
        this.value = value;
        this.dataType = dataType;
        this.valueRank = valueRank;
        this.arrayDimensions = arrayDimensions;
        this.isAbstract = isAbstract;
    }

    public BaseNodeAttributes getBaseNodeAttributes() {
        return baseNodeAttributes;
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

    public static VariableTypeNodeAttributes fromGenerated(GeneratedUAVariableType generated, UaNodeSetParser parser) {
        BaseNodeAttributes baseNodeAttributes = BaseNodeAttributes.fromGenerated(generated, NodeClass.VariableType);

        Optional<DataValue> value = value(generated.getValue(), parser.getMarshaller());
        NodeId dataType = AttributeUtil.parseDataType(generated.getDataType(), parser.getAliases());
        int valueRank = generated.getValueRank();
        Optional<UnsignedInteger[]> arrayDimensions = Optional.of(new UnsignedInteger[0]); // TODO gNode.getArrayDimensions();
        boolean isAbstract = generated.isIsAbstract();

        return new VariableTypeNodeAttributes(baseNodeAttributes, value, dataType, valueRank, arrayDimensions, isAbstract);
    }

    private static Optional<DataValue> value(GeneratedUAVariableType.GeneratedValue gValue, Marshaller marshaller) {
        if (gValue == null) return Optional.empty();

        return Optional.of(AttributeUtil.parseValue(gValue.getAny(), marshaller));
    }

}
