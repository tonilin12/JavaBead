package task.compulsory;

import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


public class MultiProducer {
    String error1="given amount is smaller than 0";


   public interface  customLambda
   {
       public String doTask(IntStream amounts,Stream<String>contents);
   }

   public String getError1()
   {
       return error1;
   }

      public static Supplier<customLambda>multiProducerFactory=()->{
              return (amounts, contents)
                      -> processMultiProducer(amounts, contents);
      };
    private static  String processMultiProducer(
            IntStream amounts, Stream<String> contents
    ) {
        long startTime = System.currentTimeMillis();
        var localAmounts = amounts.iterator();
        var localContents = contents.iterator();
        StringJoiner joiner = new StringJoiner(",");

        while (localContents.hasNext() && localContents.hasNext()) {
            int k = localAmounts.next();
            String s = String.valueOf(localContents.next());
            if (k > 0) {
                IntStream.range(0, k).forEach(j -> joiner.add(s));
            }
            if (System.currentTimeMillis() - startTime > 10000) {
                break;
            }
        }
        return joiner.toString();
    }

}
