package codeset.hazelcast.codegen;

import codeset.hazelcast.codegen.xmi.XmiModel;

import com.sun.codemodel.JCodeModel;

public interface Generator {

    void generate(XmiModel xmiModel, JCodeModel codeModel);

}
