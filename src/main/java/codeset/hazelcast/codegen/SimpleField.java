package codeset.hazelcast.codegen;

import static codeset.hazelcast.codegen.utils.Utils.getClassById;
import static codeset.hazelcast.codegen.utils.Utils.getPortables;

import java.util.List;

import org.w3c.dom.Node;

import codeset.hazelcast.codegen.xmi.XmiModel;
import codeset.hazelcast.codegen.xmi.XmiTypeMapping;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

public class SimpleField implements Generator {

    @Override
    public void generate(XmiModel xmiModel, JCodeModel codeModel) {

        for(JDefinedClass cls : getPortables(codeModel)) {

            String id = cls.metadata.toString();
            Node classNode = xmiModel.getNodeById(id);

            JDefinedClass generatedClass = getClassById(codeModel, id);
            buildSimpleFields(xmiModel, codeModel, classNode, generatedClass);

        }

    }

    private void buildSimpleFields(XmiModel xmiModel, JCodeModel codeModel, Node classNode, JDefinedClass generatedClass) {

        List<Node> fieldNodes = xmiModel.getSimpleFields(classNode);
        for(Node fieldNode : fieldNodes) {
            createField(xmiModel, codeModel, generatedClass, fieldNode);
        }

    }

    private void createField(XmiModel xmiModel, JCodeModel codeModel, JDefinedClass generatedClass, Node fieldNode) {

        String fieldName = xmiModel.getName(fieldNode);
        String fieldTypeId = xmiModel.getTypeId(fieldNode);
        Class<?> javaClass = XmiTypeMapping.getMapping(fieldTypeId);
        if(javaClass != null) {
            generatedClass.field(JMod.PRIVATE, javaClass, fieldName);
            return;
        }
        JDefinedClass fieldType = getClassById(codeModel, fieldTypeId);

        // Only create a field for known model types
        if(fieldType != null && fieldName != null) {

            // Don't create the field if it already exists
            if(generatedClass.fields().get(fieldName) != null) return;

            generatedClass.field(JMod.PRIVATE, fieldType, fieldName);

        }

    }

}