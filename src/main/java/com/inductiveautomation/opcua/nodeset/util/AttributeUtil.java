package com.inductiveautomation.opcua.nodeset.util;

import javax.xml.bind.Marshaller;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.ua.generated.GeneratedReference;

public class AttributeUtil {

    public static NodeId parseDataType(String dataTypeString, Map<String, NodeId> aliases) {
        try {
            return NodeId.parse(dataTypeString);
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
            return NodeId.parse(referenceType);
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
            // TODO XML de-serialization
//            JAXBElement<?> jaxbElement = JAXBElement.class.cast(value);
//
//            StringWriter sw = new StringWriter();
//            marshaller.marshal(jaxbElement, sw);
//
//            XmlElement xmlElement = new XmlElement(sw.toString());
//            XmlDecoder xmlDecoder = new XmlDecoder(xmlElement, ENCODER_CONTEXT);
//            Object o = xmlDecoder.getVariantContents();
//
//            return new DataValue(new Variant(o));
            return new DataValue(Variant.NullValue);
        } catch (Throwable t) {
            throw new RuntimeException("unable to parse Value: " + value, t);
        }
    }

}
