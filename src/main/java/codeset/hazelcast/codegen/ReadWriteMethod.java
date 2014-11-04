package codeset.hazelcast.codegen;

import static codeset.hazelcast.codegen.utils.Utils.getClassById;
import static codeset.hazelcast.codegen.utils.Utils.getPortables;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import codeset.hazelcast.codegen.xmi.XmiModel;
import codeset.hazelcast.codegen.xmi.XmiTypeMapping;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class ReadWriteMethod implements Generator {

    @Override
    public void generate(XmiModel xmiModel, JCodeModel codeModel) {

        for(JDefinedClass portableClass : getPortables(codeModel)) {
            generateReadWritePortableMethod(codeModel, xmiModel, portableClass);
        }

    }

    private void generateReadWritePortableMethod(JCodeModel codeModel, XmiModel xmiModel, JDefinedClass portableClass) {

        JMethod readMethod = portableClass.method(JMod.PUBLIC, codeModel._ref(void.class), "readPortable");
        readMethod._throws(codeModel.ref(IOException.class));
        readMethod.annotate(codeModel.ref(Override.class));
        JVar readerVar = readMethod.param(codeModel.ref(PortableReader.class), "reader");

        JMethod writeMethod = portableClass.method(JMod.PUBLIC, codeModel._ref(void.class), "writePortable");
        writeMethod._throws(codeModel.ref(IOException.class));
        writeMethod.annotate(codeModel.ref(Override.class));
        JVar writerVar = writeMethod.param(codeModel.ref(PortableWriter.class), "writer");

        String id = portableClass.metadata.toString();
        Node classNode = xmiModel.getNodeById(id);
        List<Node> fieldNodes = xmiModel.getSimpleFields(classNode);
        for(Node fieldNode : fieldNodes) {
            String fieldName = xmiModel.getName(fieldNode);
            String fieldTypeId = xmiModel.getTypeId(fieldNode);
            JDefinedClass fieldType = getClassById(codeModel, fieldTypeId);

            // Only create a field for known model types
            if (fieldType != null && fieldName != null) {
                // Check if we should replace with standard Java type
                Class<?> javaClass = XmiTypeMapping.getMapping(fieldType.name());
                String writeMethodName = null;
                String readMethodName = null;
                if (javaClass != null) {
                    writeMethodName = getHelperWriteMethodName(javaClass);
                    if(writeMethodName == null) continue;
                    readMethodName = getHelperReadMethodName(javaClass);
                    if(readMethodName == null) continue;
                } else {
                    writeMethodName = "writePortable";
                    readMethodName = "readPortable";
                }
                JFieldVar field = portableClass.fields().get(fieldName);

                JBlock hasBlock = readMethod.body()._if(readerVar.invoke("readBoolean").arg("_has__" + fieldName))._then();
                if(fieldType.name().equals("Date")) {
                    hasBlock.assign(field, JExpr._new(codeModel.ref(Date.class)).arg(readerVar.invoke(readMethodName).arg(fieldName)));
                } else {
                    hasBlock.assign(field, readerVar.invoke(readMethodName).arg(fieldName));
                }

                JBlock nullBlock = writeMethod.body()._if(field.ne(JExpr._null()))._then();
                if(fieldType.name().equals("Date")) {
                    nullBlock.add(writerVar.invoke(writeMethodName).arg(fieldName).arg(field.invoke("getTime")));
                } else {
                    nullBlock.add(writerVar.invoke(writeMethodName).arg(fieldName).arg(field));
                }
                nullBlock.add(writerVar.invoke("writeBoolean").arg("_has__" + fieldName).arg(JExpr.TRUE));
            }
        }

        boolean isParentPortable = false;
        Iterator<JClass> parentInterfaces = portableClass._extends()._implements();
        while(parentInterfaces.hasNext()) {
            JClass parentInterface = parentInterfaces.next();
            if(parentInterface.name().equals("Portable")) {
                isParentPortable = true;
                break;
            }
        }
        if(isParentPortable) {
            writeMethod.body().invoke(JExpr._super(), "writePortable").arg(writerVar);
            readMethod.body().invoke(JExpr._super(), "readPortable").arg(readerVar);
        }

    }

    private String getHelperWriteMethodName(Class<?> type) {

        if (int.class == type || Integer.class == type) {
            return "writeInt";
        }
        if (long.class == type || Long.class == type) {
            return "writeLong";
        }
        if (String.class == type) {
            return "writeUTF";
        }
        if (boolean.class == type || Boolean.class == type) {
            return "writeBoolean";
        }
        if (byte.class == type || Byte.class == type) {
            return "writeByte";
        }
        if (char.class == type || Character.class == type) {
            return "writeChar";
        }
        if (double.class == type || Double.class == type) {
            return "writeDouble";
        }
        if (float.class == type || Float.class == type) {
            return "writeFloat";
        }
        if (short.class == type || Short.class == type) {
            return "writeShort";
        }
        if (Portable.class.isAssignableFrom(type)) {
            return "writePortable";
        }
        if (type.isEnum()) {
            return "writeUTF";
        }
        if (Date.class.isAssignableFrom(type)) {
            return "writeLong";
        }
        return null;

    }

    private String getHelperReadMethodName(Class<?> type) {

        if (int.class == type || Integer.class == type) {
            return "readInt";
        }
        if (long.class == type || Long.class == type) {
            return "readLong";
        }
        if (String.class == type) {
            return "readUTF";
        }
        if (boolean.class == type || Boolean.class == type) {
            return "readBoolean";
        }
        if (byte.class == type || Byte.class == type) {
            return "readByte";
        }
        if (char.class == type || Character.class == type) {
            return "readChar";
        }
        if (double.class == type || Double.class == type) {
            return "readDouble";
        }
        if (float.class == type || Float.class == type) {
            return "readFloat";
        }
        if (short.class == type || Short.class == type) {
            return "readShort";
        }
        if (Portable.class.isAssignableFrom(type)) {
            return "readPortable";
        }
        if (type.isEnum()) {
            return "readUTF";
        }
        if (Date.class.isAssignableFrom(type)) {
            return "readLong";
        }
        return null;

    }

}
