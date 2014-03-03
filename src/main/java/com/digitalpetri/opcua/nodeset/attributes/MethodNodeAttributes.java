package com.digitalpetri.opcua.nodeset.attributes;

import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAMethod;

public class MethodNodeAttributes {

    private final NodeAttributes nodeAttributes;
    private final boolean executable;
    private final boolean userExecutable;

    public MethodNodeAttributes(NodeAttributes nodeAttributes, boolean executable, boolean userExecutable) {
        this.nodeAttributes = nodeAttributes;
        this.executable = executable;
        this.userExecutable = userExecutable;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public boolean isExecutable() {
        return executable;
    }

    public boolean isUserExecutable() {
        return userExecutable;
    }

    @Override
    public String toString() {
        return "MethodNodeAttributes{" +
                "nodeAttributes=" + nodeAttributes +
                ", executable=" + executable +
                ", userExecutable=" + userExecutable +
                '}';
    }

    public static MethodNodeAttributes fromGenerated(GeneratedUAMethod generated) {
        NodeAttributes nodeAttributes = NodeAttributes.fromGenerated(generated, NodeClass.Method);

        boolean executable = generated.isExecutable();
        boolean userExecutable = generated.isUserExecutable();

        return new MethodNodeAttributes(nodeAttributes, executable, userExecutable);
    }

}
