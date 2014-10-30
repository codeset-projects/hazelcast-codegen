package codeset.hazelcast.codegen;

import static codeset.hazelcast.codegen.utils.Utils.getPortables;

import java.util.Map.Entry;

import codeset.hazelcast.codegen.xmi.XmiModel;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

public class AccessorMethod implements Generator {

    @Override
    public void generate(XmiModel xmiModel, JCodeModel codeModel) {

        for(JDefinedClass portableClass : getPortables(codeModel)) {
            if(portableClass.isAbstract()) continue;

            for(Entry<String, JFieldVar> entry : portableClass.fields().entrySet()) {
                String fieldName = entry.getKey();
                JType fieldType = entry.getValue().type();

                JMethod setMethodVar = portableClass.method(JMod.PUBLIC, codeModel._ref(void.class), getSetMethodName(fieldName));
                setMethodVar.param(fieldType, fieldName);
                setMethodVar.body().assign(JExpr._this().ref(fieldName), JExpr.ref(fieldName));

                JMethod getMethodVar = portableClass.method(JMod.PUBLIC, fieldType, getGetMethodName(fieldName));
                getMethodVar.body()._return(entry.getValue());
            }

        }

    }

    private String getGetMethodName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private String getSetMethodName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

}
