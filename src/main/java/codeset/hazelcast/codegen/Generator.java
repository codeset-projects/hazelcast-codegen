package codeset.hazelcast.codegen;

import codeset.hazelcast.codegen.xmi.XmiModel;

import com.sun.codemodel.JCodeModel;

/**
 * Define the generator functionality.
 * 
 * @author ingemar.svensson
 *
 */
public interface Generator {

    /**
     * Generate the JCodeModel classes and their associated properties and
     * methods. The generated values are added to the provided codeModel.
     * 
     * @param xmiModel Holds the XMI model.
     * @param codeModel The codeModel to add generated elements to.
     */
    void generate(XmiModel xmiModel, JCodeModel codeModel);

}
