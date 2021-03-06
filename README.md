hazelcast-codegen
=======

This is one of those frameworks that might give you one of those "how did I survive without it" moments. Seriously, it's that good. If you have used code generators and domain driven design before you'd be like - yeah what's the big deal.

The codegen framework generates your Portable domain objects based on a UML model, typically created in Enterprise Architect or similar. The aim is to drive your implementation from a documented model, and guarantee that this model stays in sync with your implementation. There are of course other aspects to a system like user workflows, validation, UI's etc. Our framework generates code for what typically is represented by Portable domain objects. Just to be clear on that.

Most projects start with the best of intentions, and loads of technical design documents are produced to represent the domain. As the project progresses, change is introduced to your understanding of the world. Or the business comes up with new ideas. Even with the best of intentions, the documentation of the model gets out of sync.

We can help! What if the documented model is used to generate some of the code automatically? What if the model is the something living, something that business analysts, architects and business users bring into their discussions? Well then you won't have this problem.

####Typical process
1. Business Analysts, Users and Architects debate and design the domain model in UML and publishes it somewhere.
2. Developers point the Maven build to the model and run the codegen.
3. Beer is bought to celebrate the quick turn-around.

####It starts with a model
The model is expressed as a UML Class Diagram describing the static structure of the things in your domain, like what attributes they have. It also describes how they relate to each other.

We use the industry standard XMI format http://en.wikipedia.org/wiki/XMI. Make sure that the UML tool you use supports XMI version 2.4.1. It's easy to add supports for additional versions and formats and more will be added by us.

Once a UML model is agreed, someone exports it in XMI to a location which can be reached by the Maven build.

######Model Conventions
All classes in your model implements com.hazelcast.nio.serialization.Portable. This isn't something you need to shout about to your Business Analysts and Users. They couldn't and shouldn't care less. If you don't explicitly inherit from Portable, we will inject it.

These are the field data types we support:
* **Text**. Freetext, could be anything really. Translates into java.lang.String.
* **Set**. A collection of unique somethings. Translates into java.util.Set.
* **Map**. A list of values mapped by keys. Translates into java.util.Map.
* **List**. A non-unique, ordered collection of somethings. Translates into java.util.List.
* **Integer**. Whole numbers like 1. Or 2. Or 3. Translates into java.lang.Integer.
* **Decimal**. Decimal numbers like 0.2. Translates into java.lang.Double.
* **Date**. As in some calendar/time related data. Not a romantic encounter. Translates into java.util.Date.
* **Boolean**. True or False. Translates into java.lang.Boolean.

We thought it would be good to enforce this, since it will keep your models platform independent. No one wants to see java.util.ArrayList fields in your model! You can add your own type extensions however.

Classes only need to have fields. The framework will generate get and set methods. The visibility is also ignored, all fields will be private. Methods are ignored by default. If you wish to have a rich domain model with loads of functionality embedded in your domain objects, you need to look at the customization feature which allows you to decorate your generated classes after they have been built.

####Generating the classes
Command line: mvn codeset:hazelcast-codegen:1.0:generate -Dparam1 -Dparam2

Maven configuration:
```
    <build>
        <plugins>
            <plugin>
                <groupId>codeset</groupId>
                <artifactId>hazelcast-codegen</artifactId>
                <executions>
                    <execution>
                        <!-- Specify in which phase to run the code generation -->
                        <phase>generate-sources</phase>
                        <!-- Codegen Mojo goal, must be specified as per below -->
                        <goals>
                            <goal>generate</goal>
                        </goals>
                        <!-- See configuration details -->
                        <configuration>
                            <modelFile>src/test/resources/codeset-model.xml</modelFile>
                            <basePackage>model</basePackage>
                            <factoryClassName>codeset.model.TestPortableFactory</factoryClassName>
                            <factoryId>1</factoryId>
                            <classIdFrom>1</classIdFrom>
                            <target>target/generated-test-sources</target>
                            <typeMappings>
                                <EAJava_Boolean>java.lang.Boolean</EAJava_Boolean>
                            </typeMappings>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>codeset</groupId>
                    <artifactId>hazelcast-codegen</artifactId>
                    <version>1.0</version>
                </plugin>
            <plugins>
        <pluginManagement>
    </build>
```
######Configuration
* **modelFile**. The XMI model file fileName including folder path. Assumed to be starting from the root of your Maven project. Leave out any leading slashes e.g. src/model/my-model.xml.
* **basePackage**. The root package of your model e.g. com.mycompany.model.
* **factoryClassName**. The name of the Portable class factory. Must be a valid, fully qualified class name e.g. com.mycompany.model.PortableClassFactory.
* **factoryId**. The factoryId of the factory and the generated Portable classes. Defaults to 1.
* **classIdFrom**. The start of the classId range. Each Portable class will get a number from this range. Defaults to 1.
* **target**. The target location of the generated classes. Assumed to be starting from the root of your Maven project. Defaults to target/generated-sources. You would typically add this to your output jar and as a source folder in your IDE.
* **typeMappings**. Your own type mappings for the fields types we defined above such as Text and Date. The standard model types are mapped to the corresponding Java types. However, for this to work you need to import the types into your modelling tool and pick from those in your field type selection. Alternatively, you can use the vendor type such as EAJava_Boolean in your model and map it to the correct Java type. We provide Enterprise Architect mappings out of the box.

The format is simple: <model type>qualified Java type</model type>
```
    <EAJava_Boolean>java.lang.Boolean</EAJava_Boolean>
```

####Customization
