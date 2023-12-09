package task.customTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utility {
    static Random rand=new Random();
    public static <U> Supplier<Stream<U>> createStreamLambda(
            U base, UnaryOperator<U> modify)
    {
        MutableType<U> v = new MutableType<>(base);
        return () -> Stream.iterate(
                v.getThenModify(value -> modify.apply(value)),
                i -> v.modifyThenGet(value -> modify.apply(value))
        );
    }
    public static Supplier<IntStream> convertToIntStream(
            Supplier<Stream<Integer>> streamSupplier)
    {
        return () -> streamSupplier.get().mapToInt(Integer::intValue);
    }
    public static <U>String getFirstElem(Stream<U> stream)
    {
        return stream.findFirst().get().toString();
    }

    /*
    public static String generateRandomString() {
        String characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "abcdefghijklmnopqrstuvwxyz" +
                        "0123456789";
        StringBuilder stringBuilder = new StringBuilder();

        int length=rand.nextInt(1,6);
        for (int i = 0; i < length; i++) {
            int randomIndex = rand.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
    */

    public static <T> BiFunction<Stream<T>,Stream<T>,Stream<T>>
    createStreamMerger()
    {
        BiFunction<Stream<T>,Stream<T>,Stream<T>> result=(input1,input2)->{
            var iterator1=input1.iterator();
            var iterator2=input2.iterator();
            var seged=Stream.generate(()
                            ->merge1(iterator1,iterator2))
                    .flatMap(Function.identity())
                    .takeWhile(element->element!=null);
            return seged;
        };
        return result;
    }

    private static <T> Stream<T>
    merge1(Iterator<T> iterator1, Iterator<T> iterator2) {
        return Stream.concat(
                Stream.ofNullable(iterator1.hasNext() ? iterator1.next() : null),
                Stream.ofNullable(iterator2.hasNext() ? iterator2.next() : null)
        );
    }

    public static <T> Supplier<Stream<T>>
    supplierProceed(Supplier<Stream<T>> originalSupplier) {
        return () -> originalSupplier.get().skip(1);
    }

}
