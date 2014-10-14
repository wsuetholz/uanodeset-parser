package com.inductiveautomation.opcua.nodeset.attributes;

import java.util.Optional;

import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.QualifiedName;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUANode;

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class NodeAttributes {

    private final NodeId nodeId;
    private final NodeClass nodeClass;
    private final QualifiedName browseName;
    private final LocalizedText displayName;
    private final Optional<LocalizedText> description;
    private final Optional<UInteger> writeMask;
    private final Optional<UInteger> userWriteMask;

    public NodeAttributes(NodeId nodeId,
                          NodeClass nodeClass,
                          QualifiedName browseName,
                          LocalizedText displayName,
                          Optional<LocalizedText> description,
                          Optional<UInteger> writeMask,
                          Optional<UInteger> userWriteMask) {

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

    public Optional<UInteger> getWriteMask() {
        return writeMask;
    }

    public Optional<UInteger> getUserWriteMask() {
        return userWriteMask;
    }

    @Override
    public String toString() {
        return "NodeAttributes{" +
                "nodeId=" + nodeId +
                ", nodeClass=" + nodeClass +
                ", browseName=" + browseName +
                ", displayName=" + displayName +
                ", description=" + description +
                ", writeMask=" + writeMask +
                ", userWriteMask=" + userWriteMask +
                '}';
    }

    public static NodeAttributes fromGenerated(GeneratedUANode gNode, NodeClass nodeClass) {
        NodeId nodeId = NodeId.parse(gNode.getNodeId());
        QualifiedName browseName = QualifiedName.parse(gNode.getBrowseName());

        LocalizedText displayName = gNode.getDisplayName().stream()
                .findFirst()
                .map(gLocalizedText -> LocalizedText.english(gLocalizedText.getValue()))
                .orElse(LocalizedText.english(browseName.getName()));

        Optional<LocalizedText> description = gNode.getDescription().stream()
                .findFirst()
                .map(gLocalizedText -> LocalizedText.english(gLocalizedText.getValue()))
                .map(localizedText -> Optional.of(localizedText))
                .orElse(Optional.empty());

        Optional<UInteger> writeMask = Optional.of(uint(gNode.getWriteMask()));
        Optional<UInteger> userWriteMask = Optional.of(uint(gNode.getUserWriteMask()));

        return new NodeAttributes(nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask);
    }

}
