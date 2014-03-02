package com.digitalpetri.opcua.nodeset.util;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.builtintypes.XmlElement;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServerTable;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.xml.XmlDecoder;
import org.opcfoundation.ua.generated.GeneratedReference;
import org.opcfoundation.ua.utils.StackUtils;

public class AttributeUtil {

    public static final EncoderContext ENCODER_CONTEXT = new EncoderContext(
            NamespaceTable.DEFAULT,
            ServerTable.DEFAULT,
            StackUtils.getDefaultSerializer(),
            Integer.MAX_VALUE);

    public static NodeId parseDataType(String dataTypeString, Map<String, NodeId> aliases) {
        try {
            return NodeId.parseNodeId(dataTypeString);
        } catch (Throwable t) {
            if (aliases.containsKey(dataTypeString)) {
                return aliases.get(dataTypeString);
            } else {
                // Ok, last effort...
                Optional<NodeId> nodeId = Arrays.stream(Identifiers.class.getFields())
                        .filter(field -> field.getName().equals(dataTypeString))
                        .findFirst()
                        .map(field -> {
                            try {
                                return (NodeId) field.get(null);
                            } catch (Throwable ex) {
                                throw new RuntimeException("Couldn't get ReferenceTypeId for " + dataTypeString, ex);
                            }
                        });

                return nodeId.orElseThrow(RuntimeException::new);
            }
        }
    }

    public static NodeId parseReferenceTypeId(GeneratedReference gReference, Map<String, NodeId> aliases) {
        String referenceType = gReference.getReferenceType();

        try {
            return NodeId.parseNodeId(referenceType);
        } catch (Throwable t) {
            if (aliases.containsKey(referenceType)) {
                return aliases.get(referenceType);
            } else {
                // Ok, last effort...
                Optional<NodeId> nodeId = Arrays.stream(Identifiers.class.getFields())
                        .filter(field -> field.getName().equals(referenceType))
                        .findFirst()
                        .map(field -> {
                            try {
                                return (NodeId) field.get(null);
                            } catch (Throwable ex) {
                                throw new RuntimeException("Couldn't get ReferenceTypeId for " + referenceType, ex);
                            }
                        });

                return nodeId.orElseThrow(RuntimeException::new);
            }
        }
    }

    public static DataValue parseValue(Object value, Marshaller marshaller) {
        try {
            JAXBElement<?> jaxbElement = JAXBElement.class.cast(value);

            StringWriter sw = new StringWriter();
            marshaller.marshal(jaxbElement, sw);

            XmlElement xmlElement = new XmlElement(sw.toString());
            XmlDecoder xmlDecoder = new XmlDecoder(xmlElement, ENCODER_CONTEXT);
            Object o = xmlDecoder.getVariantContents();

            return new DataValue(new Variant(o));
        } catch (Throwable t) {
            throw new RuntimeException("unable to parse Value: " + value, t);
        }
    }

}
