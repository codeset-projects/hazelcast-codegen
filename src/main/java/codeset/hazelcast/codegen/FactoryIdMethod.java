package codeset.hazelcast.codegen;

import static codeset.hazelcast.codegen.utils.Utils.getPortables;
import codeset.hazelcast.codegen.xmi.XmiModel;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

/**
 * Generate a getFactoryId() method on each Portable class.
 * 
 * Ignores abstract classes.
 * 
 * @author ingemar.svensson
 *
 */
public class FactoryIdMethod implements Generator {

    /**
     * The value returned in the getFactoryId method, configured in the build.
     */
    private int factoryId;

    /**
     * Construct a FactoryId generator for the provided factoryId.
     * @param factoryId
     */
    public FactoryIdMethod(int factoryId) {
        this.factoryId = factoryId;
    }

    /**
     * Generate getFactoryId() method which returns the factoryId configuration value.
     * 
     * @param xmiModel Holds the XMI model.
     * @param codeModel Holds the generated Portable classes.
     */
    @Override
    public void generate(XmiModel xmiModel, JCodeModel codeModel) {

        for(JDefinedClass portableClass : getPortables(codeModel)) {
            if(portableClass.isAbstract()) continue;
            JMethod methodVar = portableClass.method(JMod.PUBLIC, int.class, "getFactoryId");
            methodVar.annotate(codeModel.ref(Override.class));
            methodVar.body()._return(JExpr.lit(factoryId));
        }

    }

}
