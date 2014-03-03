package com.digitalpetri.opcua.nodeset.attributes;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAView;

public class ViewNodeAttributes {

    private final NodeAttributes nodeAttributes;

    private final boolean containsNoLoops;
    private final UnsignedByte eventNotifier;

    public ViewNodeAttributes(NodeAttributes nodeAttributes,
                              boolean containsNoLoops,
                              UnsignedByte eventNotifier) {

        this.nodeAttributes = nodeAttributes;
        this.containsNoLoops = containsNoLoops;
        this.eventNotifier = eventNotifier;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public boolean isContainsNoLoops() {
        return containsNoLoops;
    }

    public UnsignedByte getEventNotifier() {
        return eventNotifier;
    }

    @Override
    public String toString() {
        return "ViewNodeAttributes{" +
                "nodeAttributes=" + nodeAttributes +
                ", containsNoLoops=" + containsNoLoops +
                ", eventNotifier=" + eventNotifier +
                '}';
    }

    public static ViewNodeAttributes fromGenerated(GeneratedUAView generated) {
        NodeAttributes nodeAttributes = NodeAttributes.fromGenerated(generated, NodeClass.View);

        boolean containsNoLoops = generated.isContainsNoLoops();
        UnsignedByte eventNotifier = new UnsignedByte(generated.getEventNotifier());

        return new ViewNodeAttributes(nodeAttributes, containsNoLoops, eventNotifier);
    }

}
