package com.digitalpetri.opcua.nodeset;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.List;

import com.digitalpetri.opcua.nodeset.attributes.*;
import org.junit.Before;

public class UaNodeSetParserTest {

    NodeBuilder<Node, Reference> nodeBuilder;
    ReferenceBuilder<Reference> referenceBuilder;

    @org.junit.Test
    public void testParseNodeSet2() throws Exception {
        parse("Opc.Ua.NodeSet2.xml");
    }

    @org.junit.Test
    public void testParseNodeSet2_Part3() throws Exception {
        parse("Opc.Ua.NodeSet2.Part3.xml");
    }

    @org.junit.Test
    public void testParseNodeSet2_Part4() throws Exception {
        parse("Opc.Ua.NodeSet2.Part4.xml");
    }

    @org.junit.Test
    public void testParseNodeSet2_Part5() throws Exception {
        parse("Opc.Ua.NodeSet2.Part5.xml");
    }

    @org.junit.Test
    public void testParseNodeSet2_Part9() throws Exception {
        parse("Opc.Ua.NodeSet2.Part9.xml");
    }

    @org.junit.Test
    public void testParseNodeSet2_Part10() throws Exception {
        parse("Opc.Ua.NodeSet2.Part10.xml");
    }

    @org.junit.Test
    public void testParseNodeSet2_Part11() throws Exception {
        parse("Opc.Ua.NodeSet2.Part11.xml");
    }

    @org.junit.Test
    public void testParseNodeSet2_Part13() throws Exception {
        parse("Opc.Ua.NodeSet2.Part13.xml");
    }

    private void parse(String nodeSetFilename) throws JAXBException {
        InputStream nodeSetXml = getClass().getClassLoader().getResourceAsStream(nodeSetFilename);
        UaNodeSetParser<Node, Reference> parser = new UaNodeSetParser<>(nodeBuilder, referenceBuilder, nodeSetXml);

        List<Node> nodes = parser.parse();

        System.out.println("Parsed " + nodeSetFilename + " and generated " + nodes.size() + " nodes.");
    }

    @Before
    public void setUp() {
        nodeBuilder = new NodeBuilder<Node, Reference>() {
            @Override
            public Node buildMethodNode(MethodNodeAttributes attributes, List<Reference> references) {
                return new Node();
            }

            @Override
            public Node buildObjectNode(ObjectNodeAttributes attributes, List<Reference> references) {
                return new Node();
            }

            @Override
            public Node buildReferenceTypeNode(ReferenceTypeNodeAttributes attributes, List<Reference> references) {
                return new Node();
            }

            @Override
            public Node buildVariableNode(VariableNodeAttributes attributes, List<Reference> references) {
                return new Node();
            }

            @Override
            public Node buildDataTypeNode(DataTypeNodeAttributes attributes, List<Reference> references) {
                return new Node();
            }

            @Override
            public Node buildObjectTypeNode(ObjectTypeNodeAttributes attributes, List<Reference> references) {
                return new Node();
            }

            @Override
            public Node buildVariableTypeNode(VariableTypeNodeAttributes attributes, List<Reference> references) {
                return new Node();
            }

            @Override
            public Node buildViewNode(ViewNodeAttributes attributes, List<Reference> references) {
                return new Node();
            }
        };

        referenceBuilder = (sourceNodeId, referenceTypeId, targetNodeId, targetNodeClass, forward) -> {
            Reference reference =
                    new Reference(sourceNodeId, referenceTypeId, targetNodeId, targetNodeClass, forward);

            return reference;
        };

    }

}