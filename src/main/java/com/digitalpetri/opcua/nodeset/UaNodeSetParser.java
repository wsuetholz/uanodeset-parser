package com.digitalpetri.opcua.nodeset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.*;
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

    private final Map<String, NodeId> aliasMap = new HashMap<>();
    private final Map<NodeId, List<ReferenceType>> referencesMap = new HashMap<>();

    private final UANodeSet nodeSet;
    private final Marshaller marshaller;

    private final NodeBuilder<NodeType, ReferenceType> nodeBuilder;
    private final ReferenceBuilder<ReferenceType> referenceBuilder;

    public UaNodeSetParser(NodeBuilder<NodeType, ReferenceType> nodeBuilder,
                           ReferenceBuilder<ReferenceType> referenceBuilder,
                           InputStream nodeSetXml) throws JAXBException {

        this.nodeBuilder = nodeBuilder;
        this.referenceBuilder = referenceBuilder;

        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        marshaller = jaxbContext.createMarshaller();

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        nodeSet = UANodeSet.class.cast(unmarshaller.unmarshal(nodeSetXml));

        nodeSet.getAliases().getAlias().stream().forEach(a -> {
            aliasMap.put(a.getAlias(), NodeId.parseNodeId(a.getValue()));
        });

        nodeSet.getUAObjectOrUAVariableOrUAMethod().stream().forEach(gNode -> {
            NodeId sourceNodeId = NodeId.parseNodeId(gNode.getNodeId());

            gNode.getReferences().getReference().forEach(gReference -> {
                Map<NodeId, ReferenceType> references = referenceAndInverse(sourceNodeId, gReference);

                references.keySet().stream().forEach(nodeId -> {
                    ReferenceType reference = references.get(nodeId);
                    if (referencesMap.containsKey(nodeId)) {
                        referencesMap.get(nodeId).add(reference);
                    } else {
                        List<ReferenceType> l = new ArrayList<>(1);
                        l.add(reference);
                        referencesMap.put(nodeId, l);
                    }
                });
            });
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

    public Map<String, NodeId> getAliasMap() {
        return aliasMap;
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
        List<ReferenceType> references = referencesMap.get(attributes.getBaseNodeAttributes().getNodeId());

        return nodeBuilder.buildDataTypeNode(attributes, references);
    }

    private NodeType methodNode(GeneratedUAMethod generated) {
        MethodNodeAttributes attributes = MethodNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referencesMap.get(attributes.getBaseNodeAttributes().getNodeId());

        return nodeBuilder.buildMethodNode(attributes, references);
    }

    private NodeType objectNode(GeneratedUAObject generated) {
        ObjectNodeAttributes attributes = ObjectNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referencesMap.get(attributes.getBaseNodeAttributes().getNodeId());

        return nodeBuilder.buildObjectNode(attributes, references);
    }

    private NodeType objectTypeNode(GeneratedUAObjectType generated) {
        ObjectTypeNodeAttributes attributes = ObjectTypeNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referencesMap.get(attributes.getBaseNodeAttributes().getNodeId());

        return nodeBuilder.buildObjectTypeNode(attributes, references);
    }

    private NodeType referenceTypeNode(GeneratedUAReferenceType generated) {
        ReferenceTypeNodeAttributes attributes = ReferenceTypeNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referencesMap.get(attributes.getBaseNodeAttributes().getNodeId());

        return nodeBuilder.buildReferenceTypeNode(attributes, references);
    }

    private NodeType variableNode(GeneratedUAVariable generated) {
        VariableNodeAttributes attributes = VariableNodeAttributes.fromGenerated(generated, this);
        List<ReferenceType> references = referencesMap.get(attributes.getBaseNodeAttributes().getNodeId());

        return nodeBuilder.buildVariableNode(attributes, references);
    }

    private NodeType variableTypeNode(GeneratedUAVariableType generated) {
        VariableTypeNodeAttributes attributes = VariableTypeNodeAttributes.fromGenerated(generated, this);
        List<ReferenceType> references = referencesMap.get(attributes.getBaseNodeAttributes().getNodeId());

        return nodeBuilder.buildVariableTypeNode(attributes, references);
    }

    private NodeType viewNode(GeneratedUAView generated) {
        ViewNodeAttributes attributes = ViewNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referencesMap.get(attributes.getBaseNodeAttributes().getNodeId());

        return nodeBuilder.buildViewNode(attributes, references);
    }

    private Map<NodeId, ReferenceType> referenceAndInverse(NodeId sourceNodeId, GeneratedReference gReference) {
        NodeId referenceTypeId = AttributeUtil.parseReferenceTypeId(gReference, aliasMap);

        /*
         * Create the reference...
         */
        NodeId targetNodeId = NodeId.parseNodeId(gReference.getValue());

        NodeClass targetNodeClass = nodeSet.getUAObjectOrUAVariableOrUAMethod().stream()
                .filter(gNode -> gNode.getNodeId().equals(gReference.getValue()))
                .findFirst()
                .map(gNode -> nodeClass(gNode))
                .orElse(NodeClass.Unspecified);

        boolean forward = gReference.isIsForward();

        ReferenceType reference = referenceBuilder.buildReference(
                sourceNodeId, referenceTypeId, new ExpandedNodeId(targetNodeId), targetNodeClass, forward);

        /*
         * Create the inverse of the reference...
         */
        NodeClass sourceNodeClass = nodeSet.getUAObjectOrUAVariableOrUAMethod().stream()
                .filter(gNode -> gNode.getNodeId().equals(sourceNodeId.toString()))
                .findFirst()
                .map(gNode -> nodeClass(gNode))
                .orElse(NodeClass.Unspecified);

        ReferenceType inverseReference = referenceBuilder.buildReference(
                targetNodeId, referenceTypeId, new ExpandedNodeId(sourceNodeId), sourceNodeClass, !forward);

        Map<NodeId, ReferenceType> references = new HashMap<>(2);
        references.put(sourceNodeId, reference);
        references.put(NodeId.parseNodeId(gReference.getValue()), inverseReference);
        return references;
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
