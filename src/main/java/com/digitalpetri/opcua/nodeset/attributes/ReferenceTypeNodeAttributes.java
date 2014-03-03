package com.digitalpetri.opcua.nodeset.attributes;

import java.util.Optional;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAReferenceType;

public class ReferenceTypeNodeAttributes {

    private final NodeAttributes nodeAttributes;
    private final boolean isAbstract;
    private final boolean symmetric;
    private final Optional<LocalizedText> inverseName;

    public ReferenceTypeNodeAttributes(NodeAttributes nodeAttributes,
                                       boolean isAbstract,
                                       boolean symmetric,
                                       Optional<LocalizedText> inverseName) {

        this.nodeAttributes = nodeAttributes;
        this.isAbstract = isAbstract;
        this.symmetric = symmetric;
        this.inverseName = inverseName;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isSymmetric() {
        return symmetric;
    }

    public Optional<LocalizedText> getInverseName() {
        return inverseName;
    }

    @Override
    public String toString() {
        return "ReferenceTypeNodeAttributes{" +
                "nodeAttributes=" + nodeAttributes +
                ", isAbstract=" + isAbstract +
                ", symmetric=" + symmetric +
                ", inverseName=" + inverseName +
                '}';
    }

    public static ReferenceTypeNodeAttributes fromGenerated(GeneratedUAReferenceType generated) {
        NodeAttributes nodeAttributes = NodeAttributes.fromGenerated(generated, NodeClass.ReferenceType);

        boolean isAbstract = generated.isIsAbstract();
        boolean symmetric = generated.isSymmetric();

        Optional<LocalizedText> inverseName = generated.getInverseName().stream()
                .findFirst()
                .map(gLocalizedText -> LocalizedText.english(gLocalizedText.getValue()))
                .map(localizedText -> Optional.of(localizedText))
                .orElse(Optional.empty());

        return new ReferenceTypeNodeAttributes(nodeAttributes, isAbstract, symmetric, inverseName);
    }

}
