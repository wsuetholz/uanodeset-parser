package com.digitalpetri.opcua.nodeset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.digitalpetri.opcua.nodeset.attributes.*;
import com.digitalpetri.opcua.nodeset.util.AttributeUtil;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServerTable;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.generated.*;
import org.opcfoundation.ua.utils.StackUtils;

public class UaNodeSetParser<NodeType, ReferenceType> {

    public static final EncoderContext ENCODER_CONTEXT = new EncoderContext(
            NamespaceTable.DEFAULT,
            ServerTable.DEFAULT,
            StackUtils.getDefaultSerializer(),
            Integer.MAX_VALUE);

    private final Map<String, NodeId> aliases = new HashMap<>();

    private final UANodeSet nodeSet;
    private final Unmarshaller unmarshaller;
    private final Marshaller marshaller;

    private final NodeBuilder<NodeType, ReferenceType> nodeBuilder;
    private final ReferenceBuilder<ReferenceType> referenceBuilder;

    public UaNodeSetParser(NodeBuilder<NodeType, ReferenceType> nodeBuilder,
                           ReferenceBuilder<ReferenceType> referenceBuilder,
                           InputStream nodeSetXml) throws JAXBException {

        this.nodeBuilder = nodeBuilder;
        this.referenceBuilder = referenceBuilder;

        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);

        unmarshaller = jaxbContext.createUnmarshaller();
        marshaller = jaxbContext.createMarshaller();

        nodeSet = UANodeSet.class.cast(unmarshaller.unmarshal(nodeSetXml));

        nodeSet.getAliases().getAlias().stream().forEach(a -> {
            aliases.put(a.getAlias(), NodeId.parseNodeId(a.getValue()));
        });
    }

    public List<NodeType> parse() {
        return nodeSet.getUAObjectOrUAVariableOrUAMethod().stream()
                .map(this::buildNode)
                .map(n -> Optional.ofNullable(n))
                .filter(o -> o.isPresent())
                .map(o -> o.get())
                .collect(Collectors.toList());
    }

    public Map<String, NodeId> getAliases() {
        return aliases;
    }

    public Marshaller getMarshaller() {
        return marshaller;
    }

    private NodeType buildNode(GeneratedUANode gNode) {
        if (gNode instanceof GeneratedUADataType) return dataTypeNode((GeneratedUADataType) gNode);
        if (gNode instanceof GeneratedUAMethod) return methodNode((GeneratedUAMethod) gNode);
        if (gNode instanceof GeneratedUAObject) return objectNode((GeneratedUAObject) gNode);
        if (gNode instanceof GeneratedUAObjectType) return objectTypeNode((GeneratedUAObjectType) gNode);
        if (gNode instanceof GeneratedUAReferenceType) return referenceTypeNode((GeneratedUAReferenceType) gNode);
        if (gNode instanceof GeneratedUAVariable) return variableNode((GeneratedUAVariable) gNode);
        if (gNode instanceof GeneratedUAVariableType) return variableTypeNode((GeneratedUAVariableType) gNode);
        if (gNode instanceof GeneratedUAView) return viewNode((GeneratedUAView) gNode);

        throw new RuntimeException("unexpected node: " + gNode);
    }

    private NodeType dataTypeNode(GeneratedUADataType generated) {
        DataTypeNodeAttributes attributes = DataTypeNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = references(attributes.getBaseNodeAttributes().getNodeId(), generated);

        return nodeBuilder.buildDataTypeNode(attributes, references);
    }

    private NodeType methodNode(GeneratedUAMethod generated) {
        MethodNodeAttributes attributes = MethodNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = references(attributes.getBaseNodeAttributes().getNodeId(), generated);

        return nodeBuilder.buildMethodNode(attributes, references);
    }

    private NodeType objectNode(GeneratedUAObject generated) {
        ObjectNodeAttributes attributes = ObjectNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = references(attributes.getBaseNodeAttributes().getNodeId(), generated);

        return nodeBuilder.buildObjectNode(attributes, references);
    }

    private NodeType objectTypeNode(GeneratedUAObjectType generated) {
        ObjectTypeNodeAttributes attributes = ObjectTypeNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = references(attributes.getBaseNodeAttributes().getNodeId(), generated);

        return nodeBuilder.buildObjectTypeNode(attributes, references);
    }

    private NodeType referenceTypeNode(GeneratedUAReferenceType generated) {
        ReferenceTypeNodeAttributes attributes = ReferenceTypeNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = references(attributes.getBaseNodeAttributes().getNodeId(), generated);

        return nodeBuilder.buildReferenceTypeNode(attributes, references);
    }

    private NodeType variableNode(GeneratedUAVariable generated) {
        VariableNodeAttributes attributes = VariableNodeAttributes.fromGenerated(generated, this);
        List<ReferenceType> references = references(attributes.getBaseNodeAttributes().getNodeId(), generated);

        return nodeBuilder.buildVariableNode(attributes, references);
    }

    private NodeType variableTypeNode(GeneratedUAVariableType generated) {
        VariableTypeNodeAttributes attributes = VariableTypeNodeAttributes.fromGenerated(generated, this);
        List<ReferenceType> references = references(attributes.getBaseNodeAttributes().getNodeId(), generated);

        return nodeBuilder.buildVariableTypeNode(attributes, references);
    }

    private NodeType viewNode(GeneratedUAView generated) {
        ViewNodeAttributes attributes = ViewNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = references(attributes.getBaseNodeAttributes().getNodeId(), generated);

        return nodeBuilder.buildViewNode(attributes, references);
    }

    private List<ReferenceType> references(NodeId sourceNodeId, GeneratedUANode generated) {
        return generated.getReferences().getReference().stream()
                .map(gReference -> reference(sourceNodeId, gReference))
                .collect(Collectors.toList());
    }

    private ReferenceType reference(NodeId sourceNodeId, GeneratedReference gReference) {
        NodeId referenceTypeId = AttributeUtil.parseReferenceTypeId(gReference, aliases);

        ExpandedNodeId targetNodeId = new ExpandedNodeId(NodeId.parseNodeId(gReference.getValue()));

        NodeClass targetNodeClass = nodeSet.getUAObjectOrUAVariableOrUAMethod().stream()
                .filter(gNode -> gNode.getNodeId().equals(gReference.getValue()))
                .findFirst()
                .map(gNode -> nodeClass(gNode))
                .orElse(NodeClass.Unspecified);

        boolean forward = gReference.isIsForward();

        return referenceBuilder.buildReference(
                sourceNodeId, referenceTypeId, targetNodeId, targetNodeClass, forward);
    }

    private NodeClass nodeClass(GeneratedUANode gNode) {
        if (gNode instanceof GeneratedUADataType) return NodeClass.DataType;
        if (gNode instanceof GeneratedUAMethod) return NodeClass.Method;
        if (gNode instanceof GeneratedUAObject) return NodeClass.Object;
        if (gNode instanceof GeneratedUAObjectType) return NodeClass.ObjectType;
        if (gNode instanceof GeneratedUAReferenceType) return NodeClass.ReferenceType;
        if (gNode instanceof GeneratedUAVariable) return NodeClass.Variable;
        if (gNode instanceof GeneratedUAVariableType) return NodeClass.VariableType;
        if (gNode instanceof GeneratedUAView) return NodeClass.View;

        return NodeClass.Unspecified;
    }

}
