package codeset.hazelcast.codegen;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mockito;

import codeset.hazelcast.codegen.xmi.XmiModel;

import com.hazelcast.nio.serialization.Portable;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JType;

public class FactoryIdTest {

    @Test
    public void testGenerate() throws JClassAlreadyExistsException {

        XmiModel xmiModel = Mockito.mock(XmiModel.class);

        JCodeModel codeModel = new JCodeModel();
        JDefinedClass portable1 = codeModel._class("codeset.model.TestPortable1");
        portable1._implements(Portable.class);
        JDefinedClass portable2 = codeModel._class("codeset.model.TestPortable2");
        portable2._implements(Portable.class);
        JDefinedClass portable3 = codeModel._class("codeset.model.TestPortable3");
        portable3._implements(Portable.class);

        int factoryId = 100;

        FactoryIdMethod factoryIdMethod = new FactoryIdMethod(factoryId);
        factoryIdMethod.generate(xmiModel, codeModel);

        JDefinedClass result1 = codeModel._getClass("codeset.model.TestPortable1");
        JDefinedClass result2 = codeModel._getClass("codeset.model.TestPortable2");
        JDefinedClass result3 = codeModel._getClass("codeset.model.TestPortable3");

        // check if all gets and sets have been generated
        assertNotNull(result1.getMethod("getFactoryId", new JType[] {}));
        assertNotNull(result1.getMethod("getFactoryId", new JType[] {}).type().equals(codeModel.ref(Integer.class)));

        assertNotNull(result2.getMethod("getFactoryId", new JType[] {}));
        assertNotNull(result2.getMethod("getFactoryId", new JType[] {}).type().equals(codeModel.ref(Integer.class)));

        assertNotNull(result3.getMethod("getFactoryId", new JType[] {}));
        assertNotNull(result3.getMethod("getFactoryId", new JType[] {}).type().equals(codeModel.ref(Integer.class)));


    }

}
