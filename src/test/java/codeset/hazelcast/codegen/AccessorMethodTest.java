package codeset.hazelcast.codegen;

import org.junit.Test;
import org.mockito.Mockito;

import codeset.hazelcast.codegen.xmi.XmiModel;

import com.sun.codemodel.JCodeModel;

public class AccessorMethodTest {

    @Test
    public void testGenerate() {

        XmiModel xmiModel = Mockito.mock(XmiModel.class);
        
        JCodeModel codeModel = new JCodeModel();
        AccessorMethod accessorMethod = new AccessorMethod();
        accessorMethod.generate(xmiModel, codeModel);

    }

}
