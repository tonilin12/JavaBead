package task.test;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import task.customTypes.HelperClass;
import task.customTypes.TestSampleClass;
import task.customTypes.Utility;
import task.extention.MultiProducer2;
import task.extention.MultiProducer2State;
import task.extention.customProducer.LambdaInput;
import task.extention.customProducer.LambdaOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MultiProducerTest2 {

    public String testHelper1(String text,int decisionCount,int loopCount)
    {
        var states= Utility.createStreamLambda(
          MultiProducer2State.KEEP,x->x
        );
        Function<LambdaInput,LambdaOutput> baseLambda
                =MultiProducer2.generateCachedMultiProducer();

        LambdaInput input=new LambdaInput(decisionCount,states);
        LambdaOutput output=baseLambda.apply(input);
        var outputLambda=output.getValue();
        Supplier<Stream<String>> outputLambdaResult=null;
        for (int i = 0; i <loopCount; i++) {
            outputLambdaResult= outputLambda.apply(text);
        }
       var result=Utility.getFirstElem(outputLambdaResult.get());
        result=result==""?"<üres>":result;
        return result;
    }

    @Before
    public void setup()
    {
        flipDecisionLambda = HelperClass.createflipDecisionLambda();
        bunchDecisionLambda =HelperClass.createBunchDecisionLambda();
    }

    @ParameterizedTest
    @CsvSource({"a,10,3", "b,10000,1000", "xyz,8,4", "hello,15,7"})
    public void testConstant(String input, int param1, int param2) {
        // Use the provided parameters in your test
        assertEquals("<üres>", testHelper1(input, param1, param2));
    }

    Supplier< Stream<MultiProducer2State>>flipDecisionLambda;
    Supplier< Stream<MultiProducer2State>> bunchDecisionLambda;

    @Test
    public void flip20() {
        assertEquals(TestSampleClass.getFlipExpectedValues()
        , flipDecisionLambda.get().limit(20)
                        .collect(Collectors.toList()));
    }
    @Test
    public void bunch20() {
        assertEquals(TestSampleClass.getBunchExpectedValues()
                , bunchDecisionLambda.get().limit(20)
                        .collect(Collectors.toList()));
    }
    @Test
    public void oneOfAB()
    {
        Supplier<Stream<String>>
                lambda1= Utility.createStreamLambda("a",x->x);
        Supplier<Stream<String>>
                lambda2=Utility.createStreamLambda("b",x->x);
        BiFunction<
                Supplier<Stream<String>>
                ,Supplier<Stream<String>>
                ,Stream<String>
                >
                mergedLambda= MultiProducer2.oneFromEach();
        Stream<String> result=
                mergedLambda.apply(lambda1,lambda2);

        List<String> expectedresult=new ArrayList<>(
                List.of("a","b","a","b","a","b")
        );
        assertEquals(
                expectedresult
                , result.limit(6).collect(Collectors.toList())
        );
    }

    @ParameterizedTest
    @CsvSource({"1", "80", "60"})
    public void cachedMultiProducerTest(int decisionCount)
    {

        setup();
        int limit=40;

        Supplier<Stream<String>> zipAStreamLambda=
                HelperClass.createCacheMulitpleProducerResult
                        (decisionCount,"a",flipDecisionLambda);


        Supplier<Stream<String>> bunchBStreamLambda=
                HelperClass.createCacheMulitpleProducerResult
                        (decisionCount,"b"
                                ,bunchDecisionLambda);

        BiFunction<
                Supplier<Stream<String>>
                ,Supplier<Stream<String>>
                ,Stream<String>
        >
        mergeLambda= MultiProducer2.oneFromEach();

        var result=mergeLambda
                .apply(zipAStreamLambda,bunchBStreamLambda)
                .limit(limit);


        AtomicInteger k=new AtomicInteger(1);
        StringBuilder test0data = new StringBuilder();
        result.forEach(
                x->{
                    if (k.getAndIncrement()%2==0)
                    {
                        System.out.print(x);
                        System.out.println();
                    }
                    else
                    {
                        System.out.print(x+",");
                    }
                    test0data.append(x);
                }
        );

        var expected=
                TestSampleClass.getCashedMultiProducerTestSample();
        var actual=test0data.toString();
        assertEquals(
                    expected
                ,actual
                ,"Strings differ at index " + indexOfDifference(expected, actual)
        );
    }

    private static int indexOfDifference(String str1, String str2) {
        if (str1 == str2) {
            return -1;
        }

        if (str1 == null || str2 == null) {
            return 0;
        }
        for (int i = 0; i < str1.length() && i < str2.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return i;
            }
        }

        return (str1.length() == str2.length()) ? -1 : Math.min(str1.length(), str2.length());
    }

}
