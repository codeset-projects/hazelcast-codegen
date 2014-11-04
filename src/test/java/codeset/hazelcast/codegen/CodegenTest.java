package codeset.hazelcast.codegen;

import org.junit.Test;
import org.w3c.dom.Document;

import codeset.hazelcast.codegen.write.ModelWriter;
import codeset.hazelcast.codegen.xmi.XmiModel;
import codeset.hazelcast.codegen.xmi.XmiParser;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;

public class CodegenTest {

    @Test
    public void testCodegen() throws JClassAlreadyExistsException {

        JCodeModel codeModel = new JCodeModel();
        XmiParser parser = new XmiParser();
        String fileName = "src/test/resources/codeset-model.xml";
        Document document = parser.parse(fileName);
        XmiModel xmiModel = new XmiModel(document);

        PortableClass portableClass = new PortableClass("model");
        portableClass.generate(xmiModel, codeModel);

        SimpleField simpleField = new SimpleField();
        simpleField.generate(xmiModel, codeModel);

        ReadWriteMethod readWrite = new ReadWriteMethod();
        readWrite.generate(xmiModel, codeModel);

        FactoryIdMethod factoryIdMethod = new FactoryIdMethod(100);
        factoryIdMethod.generate(xmiModel, codeModel);

        ClassIdMethod classIdMethod = new ClassIdMethod(100);
        classIdMethod.generate(xmiModel, codeModel);

        AccessorMethod accessorMethod = new AccessorMethod();
        accessorMethod.generate(xmiModel, codeModel);

        new ModelWriter().write("target/generated-test-sources", codeModel);

    }

}
