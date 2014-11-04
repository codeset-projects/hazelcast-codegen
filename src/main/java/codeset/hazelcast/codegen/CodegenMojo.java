package codeset.hazelcast.codegen;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.w3c.dom.Document;

import codeset.hazelcast.codegen.write.ModelWriter;
import codeset.hazelcast.codegen.xmi.XmiModel;
import codeset.hazelcast.codegen.xmi.XmiParser;
import codeset.hazelcast.codegen.xmi.XmiTypeMapping;

import com.sun.codemodel.JCodeModel;

@Mojo(name = "generate")
public class CodegenMojo extends AbstractMojo {

    @Parameter(defaultValue="${project}", readonly = true)
    private MavenProject project;
    @Parameter(defaultValue="${modelFile}", readonly=true)
    private String modelFile;
    @Parameter(defaultValue="${target}", readonly=true)
    private String target;
    @Parameter(defaultValue="${basePackage}", readonly=true)
    private String basePackage;
    @Parameter(defaultValue="${factoryClassName}", readonly=true)
    private String factoryClassName;
    @Parameter(defaultValue="${factoryId}", readonly=true)
    private int factoryId;
    @Parameter(defaultValue="${classIdFrom}", readonly=true)
    private int classIdFrom;
    @Parameter(defaultValue="${typeMappings}")
    private Map<String, String> typeMappings;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            File baseDir = project.getBasedir();

            getLog().info("Generating code in " + baseDir.getAbsolutePath()
                    + ". \n\tBase package: " + basePackage 
                    + ", \n\tfactoryClassName: " + factoryClassName 
                    + ", \n\tfactoryId: " + Integer.toString(factoryId) 
                    + ", \n\tclassIdFrom: " + Integer.toString(classIdFrom)
                    + ", \n\tmodelFile: " + modelFile
                    + ", \n\ttarget: " + target);
    
            JCodeModel codeModel = new JCodeModel();
            XmiParser parser = new XmiParser();
            Document document = parser.parse(modelFile);
            XmiModel xmiModel = new XmiModel(document);
            for(Entry<String, String> entry : typeMappings.entrySet()) {
                try {
                    XmiTypeMapping.addMapping(entry.getKey(), Class.forName(entry.getValue()));
                    getLog().info("Added mapping for " + entry.getKey() + " " + entry.getValue());
                } catch (ClassNotFoundException e) {
                    getLog().error("Failed to create class " + entry.getValue() + " for mapping " + entry.getKey() + ": " + e.getMessage());
                }
            }

            PortableClass portableClass = new PortableClass(basePackage);
            portableClass.generate(xmiModel, codeModel);

            SimpleField simpleField = new SimpleField();
            simpleField.generate(xmiModel, codeModel);

            ReadWriteMethod readWrite = new ReadWriteMethod();
            readWrite.generate(xmiModel, codeModel);

            FactoryIdMethod factoryIdMethod = new FactoryIdMethod(factoryId);
            factoryIdMethod.generate(xmiModel, codeModel);

            ClassIdMethod classIdMethod = new ClassIdMethod(classIdFrom);
            classIdMethod.generate(xmiModel, codeModel);

            AccessorMethod accessorMethod = new AccessorMethod();
            accessorMethod.generate(xmiModel, codeModel);

            PortableClassFactory portableClassFactory = new PortableClassFactory(factoryClassName, factoryId, classIdFrom);
            portableClassFactory.generate(xmiModel, codeModel);

            new ModelWriter().write(target, codeModel);
        } catch(CodegenException e) {
            getLog().error(e);
        }
    }

}
