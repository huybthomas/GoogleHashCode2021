# HashCode Facilitator

Hashcode facilitator is a project aimed at improving the workflow and efficiency during the Google Hash Code challenge. It has features such as automated output generation (both output files and sources.zip), and the ability to run multiple (parameterized) algorithms and automatically select the best algorithm per input file.

## How to use

The HashCode Facilitator is used through the exposed builder interface. To start building your facilitator, simply get a configurator from _**HashCodeFacilitator.configurator()**_ like:

```java
import com.stevecorp.codecontest.hashcode.facilitator.HashCodeFacilitator;

public class Example {

    public static void main(final String... args) {
        HashCodeFacilitator.configurator();
    }
    
}
```

From that point on you will need to define components in a specific order that will be used in the facilitator. For more information on how to implement these components, see the documentation in the code.

For an example of an implemented facilitator flow, check the **example** module in this project.