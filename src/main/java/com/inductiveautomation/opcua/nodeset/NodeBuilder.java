package com.inductiveautomation.opcua.nodeset;

import java.util.List;

import com.inductiveautomation.opcua.nodeset.attributes.DataTypeNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.MethodNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.ObjectNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.ObjectTypeNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.ReferenceTypeNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.VariableNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.VariableTypeNodeAttributes;
import com.inductiveautomation.opcua.nodeset.attributes.ViewNodeAttributes;

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
