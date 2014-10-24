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

Once a UML model is agreed, someone exports it in XMI to a location which can be reached by the Maven build of the project.

######Model Conventions
All classes in your model implements com.hazelcast.nio.serialization.Portable. This isn't something you need to shout about to your Business Analysts and Users. They couldn't and shouldn't care less. If you don't explicitly inherit from Portable, we will inject it.

You are only allowed to use the data types in our framework:
*Text. Freetext in a suitable format.
*Set. A collection of unique somethings.
*Map. A list of values mapped by keys.
*List. A non-unique, ordered collection of somethings.
*Integer. Whole numbers like 1. Or 2. Or 3.
*Decimal. Decimal numbers like 0.2.
*Date. As in some calendar/time related data. Not a romantic encounter.
*Boolean. True or False.



####
