package com.inductiveautomation.opcua.nodeset;

import javax.xml.bind.JAXBException;
import java.io.InputStream;

public class UaNodeSetParser<NodeType, ReferenceType> {

    private final NodeBuilder<NodeType, ReferenceType> nodeBuilder;
    private final ReferenceBuilder<ReferenceType> referenceBuilder;

    public UaNodeSetParser(NodeBuilder<NodeType, ReferenceType> nodeBuilder,
                           ReferenceBuilder<ReferenceType> referenceBuilder) {

        this.nodeBuilder = nodeBuilder;
        this.referenceBuilder = referenceBuilder;
    }

    public UaNodeSet<NodeType, ReferenceType> parse(InputStream nodeSetXml) throws JAXBException {
        return new UaNodeSet<>(nodeSetXml, nodeBuilder, referenceBuilder);
    }

}
