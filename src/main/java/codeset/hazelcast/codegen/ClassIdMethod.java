package codeset.hazelcast.codegen;

import static codeset.hazelcast.codegen.utils.Utils.getPortables;
import codeset.hazelcast.codegen.xmi.XmiModel;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

public class ClassIdMethod implements Generator {

    private int classIdFrom;

    public ClassIdMethod(int classIdFrom) {
        this.classIdFrom = classIdFrom;
    }

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
