package codeset.hazelcast.codegen.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import codeset.hazelcast.codegen.CodegenException;

import com.hazelcast.nio.serialization.Portable;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;

public class Utils {

    public static JDefinedClass newInstance(JCodeModel codeModel, String name, int modifiers) {

        try {
            return codeModel._class(modifiers, name, ClassType.CLASS);
        } catch (JClassAlreadyExistsException e) {
            throw new CodegenException("Failed to generate class because it has been generated already", e);
        }

    }

    public static JDefinedClass getClassById(JCodeModel codeModel, String id) {

        List<JDefinedClass> classes = new ArrayList<>();
        List<JPackage> packages = new ArrayList<>();
        Iterator<JPackage> packageIter = codeModel.packages();
        while(packageIter.hasNext()) {
            JPackage pkg = packageIter.next();
            packages.add(pkg);
        }
        for(JPackage pkg : packages) {
            Iterator<JDefinedClass> classIter = pkg.classes();
            while(classIter.hasNext()) {
                JDefinedClass cls = classIter.next();
                classes.add(cls);
            }
        }

        for(JDefinedClass cls : classes) {
            if(cls.metadata != null && cls.metadata.toString().equals(id)) {
                return cls;
            }
        }
        return null;

    }

    /**
     * Return a list of classes which are assignable from Portable
     * in a sorted order by class name.
     * @param codeModel
     * @return
     */
    public static List<JDefinedClass> getPortables(JCodeModel codeModel) {

        List<JDefinedClass> classes = new ArrayList<>();
        List<JPackage> packages = new ArrayList<>();
        Iterator<JPackage> packageIter = codeModel.packages();
        while(packageIter.hasNext()) {
            JPackage pkg = packageIter.next();
            packages.add(pkg);
        }
        for(JPackage pkg : packages) {
            Iterator<JDefinedClass> classIter = pkg.classes();
            while(classIter.hasNext()) {
                JDefinedClass cls = classIter.next();
                if(codeModel.ref(Portable.class).isAssignableFrom(cls)) {
                    classes.add(cls);
                }
            }
        }

        Collections.sort(classes, new Comparator<JDefinedClass>() {
            @Override
            public int compare(JDefinedClass from, JDefinedClass to) {
                return from.name().compareTo(to.name());
            }
        });
        return classes;

    }

}
