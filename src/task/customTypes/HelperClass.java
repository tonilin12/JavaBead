package task.customTypes;

import task.extention.MultiProducer2;
import task.extention.MultiProducer2State;
import task.extention.customProducer.LambdaInput;
import task.extention.customProducer.LambdaOutput;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public  class HelperClass {

    private static String getModified(
            String source,String appendText
            ,MultiProducer2State state)
    {
        if (state==MultiProducer2State.KEEP)
        {
            return source;
        }
        return source.concat(appendText);
    }
    public static Supplier<Stream<String>> createStringLambda(
            String source
            ,String appendText
            ,Supplier<Stream<MultiProducer2State>>decisionLambda2
    ) {

        Supplier<Stream<String>> result = () -> {
            var stateIterator =
                    decisionLambda2.get().iterator();

            return Stream.iterate(
                    getModified(source, appendText, stateIterator.next()),
                    s -> {
                        String modified =
                            getModified(s, appendText,  stateIterator.next());
                        return modified;
                    }
            );
        };

        return result;
    }

    public static  Supplier<Stream<MultiProducer2State>>
    createflipDecisionLambda()
    {
        Supplier<Stream<MultiProducer2State>>lambda=()->{
            Stream<MultiProducer2State>keepStream=Stream.iterate(
                    MultiProducer2State.KEEP,s->s
            );

            Stream<MultiProducer2State>extendStream=Stream.iterate(
                    MultiProducer2State.EXTEND,s->s
            );

            BiFunction<Stream<MultiProducer2State>
                    ,Stream<MultiProducer2State>
                    ,Stream<MultiProducer2State>>
             segedlambda =Utility.createStreamMerger();
            return segedlambda.apply(extendStream,keepStream);
        };
        return lambda;
    }
    public static Supplier<Stream<MultiProducer2State>>
    createBunchDecisionLambda()
    {
        Supplier<Stream<MultiProducer2State>> extendLambda =()->{
            Stream<Stream<MultiProducer2State>>
            extendStream=Stream.iterate(
                    Stream.of( MultiProducer2State.EXTEND)
                    ,s->  Stream.of( MultiProducer2State.EXTEND)
            );

            Stream<Stream<MultiProducer2State>>
            keepStream=createKeepStream();

            BiFunction< Stream<Stream<MultiProducer2State>>
                    ,  Stream<Stream<MultiProducer2State>>
                    , Stream<Stream<MultiProducer2State>>>
                    segedlambda = Utility.createStreamMerger();
            var mergedstream=
                    segedlambda.apply(extendStream,keepStream);
            var result=
                    mergedstream.flatMap(Function.identity());
            return result;
        };
        return extendLambda;
    }

    private static  Stream<Stream<MultiProducer2State>>
    createKeepStream()
    {
        Stream<Integer>segedlambda=Stream.iterate(1,x->x+1);
        var iterator1=segedlambda.iterator();
        Function<Integer,Stream<MultiProducer2State>>
        createTemporaryStream =
        (n)->{
            return Stream.iterate(MultiProducer2State.KEEP,x->x).limit(n);
        };
        Stream<Stream<MultiProducer2State>> result =
                Stream.iterate(createTemporaryStream.apply(iterator1.next())
                ,x->createTemporaryStream.apply(iterator1.next())
        );
        return result;
    }

    public static Supplier<Stream<String>>
    createCacheMulitpleProducerResult(
            int decisionCount,String appendText
             ,Supplier<Stream<MultiProducer2State>> decisions
    ) {
        Function<LambdaInput,LambdaOutput>
                baseLambda= MultiProducer2.generateCachedMultiProducer();

        LambdaInput input=new LambdaInput(decisionCount,decisions);

        Supplier<Stream<String>> outputLambdaResultSupplier = () -> {
            return Stream.generate(() -> {
                LambdaOutput output1 = baseLambda.apply(input);
                return IntStream.range(0, decisionCount)
                        .mapToObj(i ->
                                Utility.getFirstElem(output1.getValue().apply(appendText).get()))
                        .map(result -> {
                            input.proceedDecisionLambda();
                           // System.out.println(result+":"+Utility.getFirstElem(input.getDecisionLambda().get()));
                            return result.equals("") ? "<üres>" : result;
                        });
            }).flatMap(Function.identity());
        };

        return outputLambdaResultSupplier;
    }

}
