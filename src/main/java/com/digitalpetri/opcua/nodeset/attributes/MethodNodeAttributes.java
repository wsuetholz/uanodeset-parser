package com.digitalpetri.opcua.nodeset.attributes;

import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.generated.GeneratedUAMethod;

public class MethodNodeAttributes {

    private final BaseNodeAttributes baseNodeAttributes;
    private final boolean executable;
    private final boolean userExecutable;

    public MethodNodeAttributes(BaseNodeAttributes baseNodeAttributes, boolean executable, boolean userExecutable) {
        this.baseNodeAttributes = baseNodeAttributes;
        this.executable = executable;
        this.userExecutable = userExecutable;
    }

    public BaseNodeAttributes getBaseNodeAttributes() {
        return baseNodeAttributes;
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
                "baseNodeAttributes=" + baseNodeAttributes +
                ", executable=" + executable +
                ", userExecutable=" + userExecutable +
                '}';
    }

    public static MethodNodeAttributes fromGenerated(GeneratedUAMethod generated) {
        BaseNodeAttributes baseNodeAttributes = BaseNodeAttributes.fromGenerated(generated, NodeClass.Method);

        boolean executable = generated.isExecutable();
        boolean userExecutable = generated.isUserExecutable();

        return new MethodNodeAttributes(baseNodeAttributes, executable, userExecutable);
    }

}
