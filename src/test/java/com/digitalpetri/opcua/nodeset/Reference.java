package com.digitalpetri.opcua.nodeset;

import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.NodeClass;

public class Reference {

    private final NodeId sourceNodeId;
    private final NodeId referenceTypeId;
    private final ExpandedNodeId targetNodeId;
    private final NodeClass targetNodeClass;
    private final boolean forward;

    public Reference(NodeId sourceNodeId,
                     NodeId referenceTypeId,
                     ExpandedNodeId targetNodeId,
                     NodeClass targetNodeClass,
                     boolean forward) {
        this.sourceNodeId = sourceNodeId;
        this.referenceTypeId = referenceTypeId;
        this.targetNodeId = targetNodeId;
        this.targetNodeClass = targetNodeClass;
        this.forward = forward;
    }

    @Override
    public String toString() {
        return "Reference{" +
                "sourceNodeId=" + sourceNodeId +
                ", referenceTypeId=" + referenceTypeId +
                ", targetNodeId=" + targetNodeId +
                ", targetNodeClass=" + targetNodeClass +
                ", forward=" + forward +
                '}';
    }

}
