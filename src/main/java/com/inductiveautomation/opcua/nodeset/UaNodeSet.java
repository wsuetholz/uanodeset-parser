package com.inductiveautomation.opcua.nodeset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inductiveautomation.opcua.nodeset.attributes.DataTypeNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.MethodNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.ObjectNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.ObjectTypeNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.ReferenceTypeNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.VariableNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.VariableTypeNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.ViewNodeAttributes;
import com.inductiveautomation.opcua.nodeset.util.AttributeUtil;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.ua.generated.GeneratedReference;
import org.opcfoundation.ua.generated.GeneratedUADataType;
import org.opcfoundation.ua.generated.GeneratedUAMethod;
import org.opcfoundation.ua.generated.GeneratedUANode;
import org.opcfoundation.ua.generated.GeneratedUAObject;
import org.opcfoundation.ua.generated.GeneratedUAObjectType;
import org.opcfoundation.ua.generated.GeneratedUAReferenceType;
import org.opcfoundation.ua.generated.GeneratedUAVariable;
import org.opcfoundation.ua.generated.GeneratedUAVariableType;
import org.opcfoundation.ua.generated.GeneratedUAView;
import org.opcfoundation.ua.generated.ObjectFactory;
import org.opcfoundation.ua.generated.UANodeSet;

public class UaNodeSet<NodeType, ReferenceType> {

    private final Map<String, NodeId> aliasMap = new HashMap<>();
    private final Map<NodeId, NodeType> nodeMap = new HashMap<>();
    private final Map<NodeId, List<ReferenceType>> referenceMap = new HashMap<>();

    private final UANodeSet nodeSet;
    private final Marshaller marshaller;
    private final NodeBuilder<NodeType, ReferenceType> nodeBuilder;
    private final ReferenceBuilder<ReferenceType> referenceBuilder;

    UaNodeSet(InputStream nodeSetXml,
              NodeBuilder<NodeType, ReferenceType> nodeBuilder,
              ReferenceBuilder<ReferenceType> referenceBuilder) throws JAXBException {

        this.nodeBuilder = nodeBuilder;
        this.referenceBuilder = referenceBuilder;

        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        marshaller = jaxbContext.createMarshaller();

        nodeSet = UANodeSet.class.cast(jaxbContext.createUnmarshaller().unmarshal(nodeSetXml));

        nodeSet.getAliases().getAlias().stream().forEach(a -> {
            aliasMap.put(a.getAlias(), NodeId.parse(a.getValue()));
        });

        nodeSet.getUAObjectOrUAVariableOrUAMethod().stream().forEach(gNode -> {
            NodeId sourceNodeId = NodeId.parse(gNode.getNodeId());

            gNode.getReferences().getReference().forEach(gReference -> {
                Map<NodeId, ReferenceType> references = referenceAndInverse(sourceNodeId, gReference);

                references.keySet().stream().forEach(nodeId -> {
                    ReferenceType reference = references.get(nodeId);
                    if (referenceMap.containsKey(nodeId)) {
                        referenceMap.get(nodeId).add(reference);
                    } else {
                        List<ReferenceType> l = new ArrayList<>(1);
                        l.add(reference);
                        referenceMap.put(nodeId, l);
                    }
                });
            });
        });

        nodeSet.getUAObjectOrUAVariableOrUAMethod().stream()
                .filter(gNode -> gNode instanceof GeneratedUAVariableType)
                .forEach(this::buildNode);

        nodeSet.getUAObjectOrUAVariableOrUAMethod().stream()
                .filter(gNode -> gNode instanceof GeneratedUAObjectType)
                .forEach(this::buildNode);

        nodeSet.getUAObjectOrUAVariableOrUAMethod().stream()
                .filter(gNode -> !(gNode instanceof GeneratedUAVariableType) && !(gNode instanceof GeneratedUAObjectType))
                .forEach(this::buildNode);
    }

    public Map<String, NodeId> getAliasMap() {
        return aliasMap;
    }

    public Map<NodeId, NodeType> getNodeMap() {
        return nodeMap;
    }

    public Map<NodeId, List<ReferenceType>> getReferenceMap() {
        return referenceMap;
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
        List<ReferenceType> references = referenceMap.get(attributes.getNodeAttributes().getNodeId());

        NodeType node = nodeBuilder.buildDataTypeNode(attributes, references);
        nodeMap.put(attributes.getNodeAttributes().getNodeId(), node);
        return node;
    }

    private NodeType methodNode(GeneratedUAMethod generated) {
        MethodNodeAttributes attributes = MethodNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referenceMap.get(attributes.getNodeAttributes().getNodeId());

        NodeType node = nodeBuilder.buildMethodNode(attributes, references);
        nodeMap.put(attributes.getNodeAttributes().getNodeId(), node);
        return node;
    }

    private NodeType objectNode(GeneratedUAObject generated) {
        ObjectNodeAttributes attributes = ObjectNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referenceMap.get(attributes.getNodeAttributes().getNodeId());

        NodeType node = nodeBuilder.buildObjectNode(attributes, references, nodeMap);
        nodeMap.put(attributes.getNodeAttributes().getNodeId(), node);
        return node;
    }

    private NodeType objectTypeNode(GeneratedUAObjectType generated) {
        ObjectTypeNodeAttributes attributes = ObjectTypeNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referenceMap.get(attributes.getNodeAttributes().getNodeId());

        NodeType node = nodeBuilder.buildObjectTypeNode(attributes, references);
        nodeMap.put(attributes.getNodeAttributes().getNodeId(), node);
        return node;
    }

    private NodeType referenceTypeNode(GeneratedUAReferenceType generated) {
        ReferenceTypeNodeAttributes attributes = ReferenceTypeNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referenceMap.get(attributes.getNodeAttributes().getNodeId());

        NodeType node = nodeBuilder.buildReferenceTypeNode(attributes, references);
        nodeMap.put(attributes.getNodeAttributes().getNodeId(), node);
        return node;
    }

    private NodeType variableNode(GeneratedUAVariable generated) {
        VariableNodeAttributes attributes = VariableNodeAttributes.fromGenerated(generated, marshaller, aliasMap);
        List<ReferenceType> references = referenceMap.get(attributes.getNodeAttributes().getNodeId());

        NodeType node = nodeBuilder.buildVariableNode(attributes, references, nodeMap);
        nodeMap.put(attributes.getNodeAttributes().getNodeId(), node);
        return node;
    }

    private NodeType variableTypeNode(GeneratedUAVariableType generated) {
        VariableTypeNodeAttributes attributes = VariableTypeNodeAttributes.fromGenerated(generated, marshaller, aliasMap);
        List<ReferenceType> references = referenceMap.get(attributes.getNodeAttributes().getNodeId());

        NodeType node = nodeBuilder.buildVariableTypeNode(attributes, references);
        nodeMap.put(attributes.getNodeAttributes().getNodeId(), node);
        return node;
    }

    private NodeType viewNode(GeneratedUAView generated) {
        ViewNodeAttributes attributes = ViewNodeAttributes.fromGenerated(generated);
        List<ReferenceType> references = referenceMap.get(attributes.getNodeAttributes().getNodeId());

        NodeType node = nodeBuilder.buildViewNode(attributes, references);
        nodeMap.put(attributes.getNodeAttributes().getNodeId(), node);
        return node;
    }

    private Map<NodeId, ReferenceType> referenceAndInverse(NodeId sourceNodeId, GeneratedReference gReference) {
        NodeId referenceTypeId = AttributeUtil.parseReferenceTypeId(gReference, aliasMap);

        /*
         * Create the reference...
         */
        NodeId targetNodeId = NodeId.parse(gReference.getValue());

        NodeClass targetNodeClass = nodeSet.getUAObjectOrUAVariableOrUAMethod().stream()
                .filter(gNode -> gNode.getNodeId().equals(gReference.getValue()))
                .findFirst()
                .map(this::nodeClass)
                .orElse(NodeClass.Unspecified);

        boolean forward = gReference.isIsForward();

        ReferenceType reference = referenceBuilder.buildReference(
                sourceNodeId, referenceTypeId, targetNodeId.expanded(), targetNodeClass, forward);

        /*
         * Create the inverse of the reference...
         */
        NodeClass sourceNodeClass = nodeSet.getUAObjectOrUAVariableOrUAMethod().stream()
                .filter(gNode -> {
                    NodeId nodeId = NodeId.parse(gNode.getNodeId());
                    return nodeId != null && nodeId.equals(sourceNodeId);
                })
                .findFirst()
                .map(this::nodeClass)
                .orElse(NodeClass.Unspecified);

        ReferenceType inverseReference = referenceBuilder.buildReference(
                targetNodeId, referenceTypeId, sourceNodeId.expanded(), sourceNodeClass, !forward);

        Map<NodeId, ReferenceType> references = new HashMap<>(2);
        references.put(sourceNodeId, reference);
        references.put(NodeId.parse(gReference.getValue()), inverseReference);
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
