package codeset.hazelcast.codegen;

import java.util.Iterator;

import org.junit.Test;

import com.hazelcast.nio.serialization.Portable;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;

public class JCodeModelTest {

    @Test
    public void testGetPortables() throws JClassAlreadyExistsException {

        JCodeModel codeModel = new JCodeModel();
        JDefinedClass portableClass1 = codeModel._class("test.PortableClass1");
        portableClass1._extends(Portable.class);
        JDefinedClass portableClass2 = codeModel._class("test.PortableClass2");
        portableClass2._extends(portableClass1);
        JDefinedClass portableClass3 = codeModel._class("test.PortableClass3");
        portableClass3._extends(portableClass2);
        JDefinedClass normalClass = codeModel._class("test.NormalClass");

        Iterator<JPackage> packageIter = codeModel.packages();
        while(packageIter.hasNext()) {
            JPackage pkg = packageIter.next();
            Iterator<JDefinedClass> classIter = pkg.classes();
            while(classIter.hasNext()) {
                JDefinedClass cls = classIter.next();
                System.out.println(codeModel.ref(Portable.class).isAssignableFrom(cls));
            }
        }
    }

}
