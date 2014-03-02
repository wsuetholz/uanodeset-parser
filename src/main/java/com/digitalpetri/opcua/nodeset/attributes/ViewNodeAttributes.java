package com.digitalpetri.opcua.nodeset.attributes;

import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAView;

public class ViewNodeAttributes {

    private final BaseNodeAttributes baseNodeAttributes;

    private final boolean containsNoLoops;
    private final UnsignedByte eventNotifier;

    public ViewNodeAttributes(BaseNodeAttributes baseNodeAttributes,
                              boolean containsNoLoops,
                              UnsignedByte eventNotifier) {

        this.baseNodeAttributes = baseNodeAttributes;
        this.containsNoLoops = containsNoLoops;
        this.eventNotifier = eventNotifier;
    }

    public BaseNodeAttributes getBaseNodeAttributes() {
        return baseNodeAttributes;
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
                "baseNodeAttributes=" + baseNodeAttributes +
                ", containsNoLoops=" + containsNoLoops +
                ", eventNotifier=" + eventNotifier +
                '}';
    }

    public static ViewNodeAttributes fromGenerated(GeneratedUAView generated) {
        BaseNodeAttributes baseNodeAttributes = BaseNodeAttributes.fromGenerated(generated, NodeClass.View);

        boolean containsNoLoops = generated.isContainsNoLoops();
        UnsignedByte eventNotifier = new UnsignedByte(generated.getEventNotifier());

        return new ViewNodeAttributes(baseNodeAttributes, containsNoLoops, eventNotifier);
    }

}
