package task.test;

import org.junit.Test;
import task.customTypes.Utility;
import task.compulsory.MultiProducer;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiProducerTest {
    MultiProducer multiProducer = new MultiProducer();

    MultiProducer.customLambda produceFactory =
            MultiProducer.multiProducerFactory.get();

    Utility utility= new Utility();

    Supplier<IntStream> amountLambda123A =
            utility.convertToIntStream(
            utility.createStreamLambda(0, v -> v + 1));

    Supplier<IntStream> amountLambda123B =
            utility.convertToIntStream(
            utility.createStreamLambda(0, v -> v + 1));

    Supplier<Stream<String>> contentLambdaA =
            utility.createStreamLambda("", s -> s + "a");

    Supplier<Stream<String>> contentLambdaB =
            utility.createStreamLambda("", s -> s + "a");


    String[] txtA = new String[1];
    String[] txtB = new String[1];

    public String textProcess(
            String allapot,  Supplier<IntStream> amount,
            Supplier<Stream<String>> contents
    ) {
        var elemX = produceFactory.doTask(
                amount.get().limit(1),
                contents.get().limit(1)
        );

        if (allapot == null || allapot.equals("")) {
            allapot = elemX;
        } else {
            allapot = String.join(",", allapot, elemX);
        }
        return allapot;
    }

    @Test
    public void test() {
        var sample = "a,aa,aa,aaa,aaa,aaa";
        int k1 = 0;
        int k2 = 0;

        for (int i = 0; i <= 7; i++) {
            if (i % 2 == 0) {
                txtA[0] = textProcess(txtA[0], amountLambda123A, contentLambdaA);
                k1++;
            } else {
                txtB[0] = textProcess(txtB[0], amountLambda123B, contentLambdaB);
                k2++;
            }
        }

        assertEquals(sample, txtA[0]);
        assertEquals(sample, txtB[0]);
    }
}
