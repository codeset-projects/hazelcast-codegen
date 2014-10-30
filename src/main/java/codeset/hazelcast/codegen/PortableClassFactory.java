package codeset.hazelcast.codegen;

import static codeset.hazelcast.codegen.utils.Utils.getPortables;
import static codeset.hazelcast.codegen.utils.Utils.newInstance;
import codeset.hazelcast.codegen.xmi.XmiModel;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableFactory;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class PortableClassFactory implements Generator {

    private String factoryClassName;
    private int classIdFrom;
    private int factoryId;

    public PortableClassFactory(String factoryClassName, int classIdFrom, int factoryId) {

        this.factoryClassName = factoryClassName;
        this.classIdFrom = classIdFrom;
        this.factoryId = factoryId;

    }

    @Override
    public void generate(XmiModel xmiModel, JCodeModel codeModel) {

        JDefinedClass factoryClass = newInstance(codeModel, factoryClassName, JMod.PUBLIC);
        factoryClass._implements(PortableFactory.class);

        JFieldVar factoryIdVar = factoryClass.field(JMod.PUBLIC + JMod.FINAL + JMod.STATIC, int.class, "FACTORY_ID");
        factoryIdVar.init(JExpr.lit(factoryId));

        JMethod methodVar = factoryClass.method(JMod.PUBLIC, Portable.class, "create");
        methodVar.annotate(codeModel.ref(Override.class));
        JVar classIdParamVar = methodVar.param(int.class, "classId");
        JBlock methodBody = methodVar.body();

        for(JDefinedClass portableClass : getPortables(codeModel)) {
            JBlock classIdMatchVar = methodBody._if(classIdParamVar.eq(JExpr.lit(classIdFrom)))._then();
            classIdMatchVar._return(JExpr._new(portableClass));
            createClassIdMethod(codeModel, portableClass, classIdFrom++);
        }

        JInvocation exceptionVar = JExpr._new(codeModel.ref(IllegalArgumentException.class));
        JExpression messageVar = JExpr.lit("Failed to find matching class for ").plus(JExpr.ref("classId"));
        exceptionVar.arg(messageVar);
        methodBody._throw(exceptionVar);

    }

    private void createClassIdMethod(JCodeModel codeModel, JDefinedClass generatedClass, int classId) {


    }

}
