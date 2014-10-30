package codeset.hazelcast.codegen;

import static codeset.hazelcast.codegen.utils.Utils.getClassById;
import static codeset.hazelcast.codegen.utils.Utils.newInstance;

import java.util.List;

import org.w3c.dom.Node;

import codeset.hazelcast.codegen.xmi.XmiModel;
import codeset.hazelcast.codegen.xmi.XmiTypeMapping;

import com.hazelcast.nio.serialization.Portable;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

public class PortableClass implements Generator {

    private String rootPackage;

    public PortableClass(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    @Override
    public void generate(XmiModel xmiModel, JCodeModel codeModel) {

        List<Node> classNodes = xmiModel.getClassNodes();
        for(Node node : classNodes) {
            buildClass(codeModel, rootPackage, xmiModel, node);
        }
        setParent(xmiModel, codeModel);

    }

    private void buildClass(JCodeModel codeModel, String rootPackage, XmiModel xmiDocument, Node node) {

        String packageName = xmiDocument.getPackage(rootPackage, node.getParentNode(), null);
        if(!packageName.startsWith(rootPackage)) {
            return;
        }

        String name = xmiDocument.getName(node);
        String qualifiedName = packageName + "." + name;

        int mods = JMod.PUBLIC;
        if(xmiDocument.isAbstract(node)) {
            mods = mods + JMod.ABSTRACT;
        }
        JDefinedClass cls = newInstance(codeModel, qualifiedName, mods);

        // All classes should implement Portable except for default types
        if(XmiTypeMapping.get(cls.name()) == null) {
            cls._implements(Portable.class);
        }

        // XMI ID of the class is set as metadata 
        String id = xmiDocument.getId(node);
        cls.metadata = id;

    }

    private void setParent(XmiModel xmiModel, JCodeModel codeModel) {

        List<Node> classNodes = xmiModel.getClassNodes();
        for(Node classNode : classNodes) {
            String id = xmiModel.getId(classNode);
            JDefinedClass cls = getClassById(codeModel, id);

            Node parentNode = xmiModel.getParent(classNode);
            if(cls != null && parentNode != null) {
                JClass parentClass = getClassById(codeModel, parentNode.getNodeValue());
                if(parentClass != null) {
                    cls._extends(parentClass);
                }
            }
        }

    }

}
