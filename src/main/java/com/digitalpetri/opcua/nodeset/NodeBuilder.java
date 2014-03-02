package com.digitalpetri.opcua.nodeset;

import java.util.List;

import com.digitalpetri.opcua.nodeset.attributes.*;

public interface NodeBuilder<NodeType, ReferenceType> {

    NodeType buildDataTypeNode(DataTypeNodeAttributes attributes, List<ReferenceType> references);

    NodeType buildMethodNode(MethodNodeAttributes attributes, List<ReferenceType> references);

    NodeType buildObjectNode(ObjectNodeAttributes attributes, List<ReferenceType> references);

    NodeType buildObjectTypeNode(ObjectTypeNodeAttributes attributes, List<ReferenceType> references);

    NodeType buildReferenceTypeNode(ReferenceTypeNodeAttributes attributes, List<ReferenceType> references);

    NodeType buildVariableNode(VariableNodeAttributes attributes, List<ReferenceType> references);

    NodeType buildVariableTypeNode(VariableTypeNodeAttributes attributes, List<ReferenceType> references);

    NodeType buildViewNode(ViewNodeAttributes attributes, List<ReferenceType> references);

}
