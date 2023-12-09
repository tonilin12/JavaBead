package task.extention;

import task.customTypes.HelperClass;
import task.customTypes.Utility;
import task.extention.customProducer.LambdaInput;
import task.extention.customProducer.LambdaOutput;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiProducer2 {


    public static Function<LambdaInput, LambdaOutput>
    generateCachedMultiProducer()
    {
        Function<LambdaInput, LambdaOutput>cachedMultiProducer=
                new Function<>() {
                    ArrayList<String> cashe = new ArrayList<>(List.of(""));

                    @Override
                    public LambdaOutput apply(LambdaInput lambdaInput) {
                        Function<String, Supplier<Stream<String>>>
                        outputvar = (appendText) ->
                        {
                            if (cashe.size() == 1) {
                                var textCreater =
                                        HelperClass.createStringLambda(
                                                cashe.get(0)
                                                , appendText
                                                , lambdaInput.getDecisionLambda()
                                        );

                                var newElements =
                                        textCreater.get()
                                                .limit(lambdaInput.getDecisionCount())
                                                .toList();

                                cashe.addAll(newElements);
                            }
                            Supplier<Stream<String>> returnLambda = () -> {
                                return Stream.of(cashe.remove(0));
                            };
                            return returnLambda;
                        };
                        LambdaOutput output = new LambdaOutput(outputvar);
                        return output;
                    }
                };
        return cachedMultiProducer;
    }

    /* public static Function<LambdaInput, LambdaOutput> cachedMultiProduce=generateCachedMultiProducer(); */


    public static <T>BiFunction<
            Supplier<Stream<T>>
            ,Supplier<Stream<T>>
            ,Stream<T>>
    oneFromEach()
    {
              BiFunction<
                        Supplier<Stream<T>>
                        ,Supplier<Stream<T>>
                        ,Stream<T>>
               result=(lambda1,lambda2)->{
                    Stream<T> v1=lambda1.get();
                    Stream<T> v2=lambda2.get();
                    BiFunction<Stream<T>,Stream<T>,Stream<T>> segedlambda
                            =Utility.createStreamMerger();
                    return segedlambda.apply(v1,v2);
            };
              return result;
    }



}
