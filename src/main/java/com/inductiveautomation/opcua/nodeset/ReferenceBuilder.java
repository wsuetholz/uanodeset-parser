package com.inductiveautomation.opcua.nodeset;

import com.inductiveautomation.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;

public interface ReferenceBuilder<ReferenceType> {

    ReferenceType buildReference(NodeId sourceNodeId,
                                 NodeId referenceTypeId,
                                 ExpandedNodeId targetNodeId,
                                 NodeClass targetNodeClass,
                                 boolean forward);

}
