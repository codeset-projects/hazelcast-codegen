package codeset.hazelcast.codegen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import codeset.hazelcast.codegen.PortableClassFactory;
import codeset.hazelcast.codegen.write.ModelWriter;

import com.hazelcast.nio.serialization.Portable;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

public class PortableClassFactoryTest {

    @Test
    public void testGenerate() throws JClassAlreadyExistsException {

        String factoryClassName = "TestPortableFactory";
        int classIdFrom = 1000;
        int factoryId = 1000;

        PortableClassFactory generator = new PortableClassFactory(factoryClassName, classIdFrom, factoryId);
        JCodeModel codeModel = new JCodeModel();
        JDefinedClass portableClass1 = codeModel._class("test.PortableClass1");
        portableClass1._extends(Portable.class);
        JDefinedClass portableClass2 = codeModel._class("test.PortableClass2");
        portableClass2._extends(portableClass1);
        JDefinedClass portableClass3 = codeModel._class("test.PortableClass3");
        portableClass3._extends(portableClass2);
        JDefinedClass normalClass = codeModel._class("test.NormalClass");

        generator.generate(null, codeModel);

        JDefinedClass factoryClass = codeModel._getClass(factoryClassName);
        
        assertEquals("create", factoryClass.methods().iterator().next().name());

        new ModelWriter().write("target/generated-test-sources", codeModel);

    }

}
