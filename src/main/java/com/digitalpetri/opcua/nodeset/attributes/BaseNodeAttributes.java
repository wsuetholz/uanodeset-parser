package com.digitalpetri.opcua.nodeset.attributes;

import java.util.Optional;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUANode;

public class BaseNodeAttributes {

    private final NodeId nodeId;
    private final NodeClass nodeClass;
    private final QualifiedName browseName;
    private final LocalizedText displayName;
    private final Optional<LocalizedText> description;
    private final Optional<UnsignedInteger> writeMask;
    private final Optional<UnsignedInteger> userWriteMask;

    public BaseNodeAttributes(NodeId nodeId,
                              NodeClass nodeClass,
                              QualifiedName browseName,
                              LocalizedText displayName,
                              Optional<LocalizedText> description,
                              Optional<UnsignedInteger> writeMask,
                              Optional<UnsignedInteger> userWriteMask) {

        this.nodeId = nodeId;
        this.nodeClass = nodeClass;
        this.browseName = browseName;
        this.displayName = displayName;
        this.description = description;
        this.writeMask = writeMask;
        this.userWriteMask = userWriteMask;
    }

    public NodeId getNodeId() {
        return nodeId;
    }

    public NodeClass getNodeClass() {
        return nodeClass;
    }

    public QualifiedName getBrowseName() {
        return browseName;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    public Optional<LocalizedText> getDescription() {
        return description;
    }

    public Optional<UnsignedInteger> getWriteMask() {
        return writeMask;
    }

    public Optional<UnsignedInteger> getUserWriteMask() {
        return userWriteMask;
    }

    @Override
    public String toString() {
        return "BaseNodeAttributes{" +
                "nodeId=" + nodeId +
                ", nodeClass=" + nodeClass +
                ", browseName=" + browseName +
                ", displayName=" + displayName +
                ", description=" + description +
                ", writeMask=" + writeMask +
                ", userWriteMask=" + userWriteMask +
                '}';
    }

    public static BaseNodeAttributes fromGenerated(GeneratedUANode gNode, NodeClass nodeClass) {
        NodeId nodeId = NodeId.parseNodeId(gNode.getNodeId());
        QualifiedName browseName = QualifiedName.parseQualifiedName(gNode.getBrowseName());

        LocalizedText displayName = gNode.getDisplayName().stream()
                .findFirst()
                .map(gLocalizedText -> LocalizedText.english(gLocalizedText.getValue()))
                .orElse(LocalizedText.english(browseName.getName()));

        Optional<LocalizedText> description = gNode.getDescription().stream()
                .findFirst()
                .map(gLocalizedText -> LocalizedText.english(gLocalizedText.getValue()))
                .map(localizedText -> Optional.of(localizedText))
                .orElse(Optional.empty());

        Optional<UnsignedInteger> writeMask = Optional.of(new UnsignedInteger(gNode.getWriteMask()));
        Optional<UnsignedInteger> userWriteMask = Optional.of(new UnsignedInteger(gNode.getUserWriteMask()));

        return new BaseNodeAttributes(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask);
    }

}
