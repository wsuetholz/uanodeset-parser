package com.digitalpetri.opcua.nodeset;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

public interface ReferenceBuilder<ReferenceType> {

    ReferenceType buildReference(NodeId sourceNodeId,
                                 NodeId referenceTypeId,
                                 ExpandedNodeId targetNodeId,
                                 NodeClass targetNodeClass,
                                 boolean forward);

}
