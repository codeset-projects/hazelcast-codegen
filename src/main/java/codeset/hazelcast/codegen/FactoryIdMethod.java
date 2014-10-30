package codeset.hazelcast.codegen;

import static codeset.hazelcast.codegen.utils.Utils.getPortables;
import codeset.hazelcast.codegen.xmi.XmiModel;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

public class FactoryIdMethod implements Generator {

    private int factoryId;

    public FactoryIdMethod(int factoryId) {
        this.factoryId = factoryId;
    }

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
