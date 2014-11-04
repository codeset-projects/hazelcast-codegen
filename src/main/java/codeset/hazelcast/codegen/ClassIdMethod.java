package codeset.hazelcast.codegen;

import static codeset.hazelcast.codegen.utils.Utils.getPortables;
import codeset.hazelcast.codegen.xmi.XmiModel;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

/**
 * Generate a classId method for each Portable class. 
 * 
 * NOTE: The classId assigned is not guaranteed to be the same between generate 
 * runs if the model changes.
 * 
 * @author ingemar.svensson
 *
 */
public class ClassIdMethod implements Generator {

    /**
     * Starting the range of classIds assigned.
     */
    private int classIdFrom;

    /**
     * Construct a new ClassIdMethod with a mandatory classIdFrom.
     * @param classIdFrom A starting number of the classId range.
     */
    public ClassIdMethod(int classIdFrom) {
        this.classIdFrom = classIdFrom;
    }

    /**
     * Generate the getClassId() method for each Portable class.
     * 
     * Ignores abstract classes.
     * 
     * @param xmiModel Holds the XMI model.
     * @param codeModel Holds the generated Portable classes.
     */
    @Override
    public void generate(XmiModel xmiModel, JCodeModel codeModel) {

        for(JDefinedClass portableClass : getPortables(codeModel)) {
            if(portableClass.isAbstract()) continue;
            JMethod methodVar = portableClass.method(JMod.PUBLIC, int.class, "getClassId");
            methodVar.annotate(codeModel.ref(Override.class));
            JBlock methodBody = methodVar.body();
            methodBody._return(JExpr.lit(classIdFrom++));
        }

    }

}
