package task.extention.customProducer;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LambdaOutput {
    public Function<String, Supplier<Stream<String>>> getValue() {
        return output;
    }

    Function<String, Supplier<Stream<String>>> output;

    public LambdaOutput(Function<String, Supplier<Stream<String>>> output)
    {
        this.output=output;
    }
}
