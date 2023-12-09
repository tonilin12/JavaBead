package task.extention.customProducer;

import org.junit.Test;
import task.customTypes.Utility;
import task.extention.MultiProducer2State;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class LambdaInput {
    Integer decisionCount;
    public Integer getDecisionCount() {
        return decisionCount;
    }
    public Supplier<Stream<MultiProducer2State>> getDecisionLambda() {
        return decisionLambda;
    }
    Supplier<Stream<MultiProducer2State>> decisionLambda;

    public void proceedDecisionLambda()
    {
        decisionLambda= Utility.supplierProceed(decisionLambda);
    }
    public LambdaInput(
            Integer decisionCount
            ,Supplier<Stream<MultiProducer2State>>decisionLambda
    )
    {
        this.decisionLambda=decisionLambda;
        this.decisionCount=decisionCount;
    }


}