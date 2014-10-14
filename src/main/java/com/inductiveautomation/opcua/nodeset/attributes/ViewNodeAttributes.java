package com.inductiveautomation.opcua.nodeset.attributes;

import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UByte;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAView;

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

public class ViewNodeAttributes {

    private final NodeAttributes nodeAttributes;

    private final boolean containsNoLoops;
    private final UByte eventNotifier;

    public ViewNodeAttributes(NodeAttributes nodeAttributes,
                              boolean containsNoLoops,
                              UByte eventNotifier) {

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

    public UByte getEventNotifier() {
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
        UByte eventNotifier = ubyte(generated.getEventNotifier());

        return new ViewNodeAttributes(nodeAttributes, containsNoLoops, eventNotifier);
    }

}
