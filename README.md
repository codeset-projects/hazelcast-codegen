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
* Text. Freetext, could be anything really. Translates into java.lang.String.
* Set. A collection of unique somethings. Translates into java.util.Set.
* Map. A list of values mapped by keys. Translates into java.util.Map.
* List. A non-unique, ordered collection of somethings. Translates into java.util.List.
* Integer. Whole numbers like 1. Or 2. Or 3. Translates into java.lang.Integer.
* Decimal. Decimal numbers like 0.2. Translates into java.lang.Double.
* Date. As in some calendar/time related data. Not a romantic encounter. Translates into java.util.Date.
* Boolean. True or False. Translates into java.lang.Boolean.

We thought it would be good to enforce this, since it will keep your models platform independent. No one wants to see java.util.ArrayList fields in your model! You can add your own type extensions however.

Classes only need to have fields. The framework will generate get and set methods. The visibility is also ignored, all fields will be private. Methods are ignored by default. If you wish to have a rich domain model with loads of functionality embedded in your domain objects, you should use a different library for now.

####Generating the classes

####Customization
