package codeset.hazelcast.codegen;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mockito;

import codeset.hazelcast.codegen.xmi.XmiModel;

import com.hazelcast.nio.serialization.Portable;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

/**
 * Checks that all get and set methods have been generated correctly based
 * on the field in the class.
 * 
 * @author ingemar.svensson
 *
 */
public class AccessorMethodTest {

    @Test
    public void testGenerate() throws JClassAlreadyExistsException {

        XmiModel xmiModel = Mockito.mock(XmiModel.class);
        
        JCodeModel codeModel = new JCodeModel();
        JDefinedClass portable = codeModel._class("codeset.model.TestPortable");
        portable._implements(Portable.class);

        // add some fields to be checked.
        portable.field(JMod.PRIVATE, String.class, "stringProperty");
        portable.field(JMod.PRIVATE, Integer.class, "intProperty");
        portable.field(JMod.PRIVATE, Double.class, "doubleProperty");

        AccessorMethod accessorMethod = new AccessorMethod();
        accessorMethod.generate(xmiModel, codeModel);

        JDefinedClass result = codeModel._getClass("codeset.model.TestPortable");

        // check if all gets and sets have been generated
        assertNotNull(result.getMethod("getStringProperty", new JType[] {}));
        assertNotNull(result.getMethod("setStringProperty", new JType[] {codeModel.ref(String.class)}));

        assertNotNull(result.getMethod("getIntProperty", new JType[] {}));
        assertNotNull(result.getMethod("setIntProperty", new JType[] {codeModel.ref(Integer.class)}));

        assertNotNull(result.getMethod("getDoubleProperty", new JType[] {}));
        assertNotNull(result.getMethod("setDoubleProperty", new JType[] {codeModel.ref(Double.class)}));

    }

}
