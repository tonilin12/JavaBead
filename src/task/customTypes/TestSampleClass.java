package task.customTypes;

import org.junit.Test;
import task.extention.MultiProducer2State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSampleClass {
    public static String getCashedMultiProducerTestSample()
    {
        String data = "<üres>,<üres>\n" +
                "ab" +
                "ab" +
                "aa,bb\n" +
                "aa,bb\n" +
                "aaa,bb\n" +
                "aaa,bbb\n" +
                "aaaa,bbb\n" +
                "aaaa,bbb" +
                "aaaaa,bbb" +
                "aaaaa,bbbb" +
                "aaaaaa,bbbb" +
                "aaaaaa,bbbb" +
                "aaaaaaa,bbbb" +
                "aaaaaaa,bbbb" +
                "aaaaaaaa,bbbbb" +
                "aaaaaaaa,bbbbb" +
                "aaaaaaaaa,bbbbb" +
                "aaaaaaaaa,bbbbb" +
                "aaaaaaaaaa,bbbbb";

        return data.replaceAll("[,\\n]", "");
    }


    public static ArrayList<MultiProducer2State> getFlipExpectedValues()
    {
        var list=new ArrayList<MultiProducer2State>(List.of(
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP
        ));
        return list;
    }

    public static ArrayList<MultiProducer2State> getBunchExpectedValues()
    {
        var list=new ArrayList<MultiProducer2State>(List.of(
                MultiProducer2State.EXTEND, MultiProducer2State.KEEP,
                //----
                MultiProducer2State.EXTEND,
                MultiProducer2State.KEEP, MultiProducer2State.KEEP,
                //------
                MultiProducer2State.EXTEND,
                MultiProducer2State.KEEP, MultiProducer2State.KEEP,
                MultiProducer2State.KEEP,
                //-----
                MultiProducer2State.EXTEND,
                MultiProducer2State.KEEP, MultiProducer2State.KEEP,
                MultiProducer2State.KEEP,MultiProducer2State.KEEP,
                //-----
                MultiProducer2State.EXTEND,
                MultiProducer2State.KEEP, MultiProducer2State.KEEP,
                MultiProducer2State.KEEP,MultiProducer2State.KEEP,
                MultiProducer2State.KEEP
        ));
        return list;
    }
}
