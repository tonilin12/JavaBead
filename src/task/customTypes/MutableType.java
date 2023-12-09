package task.customTypes;

import java.util.function.UnaryOperator;

class MutableType<T> {
    T value;
    MutableType(T value) {
        this.value = value;
    }
    T getValue() {
        return value;
    }
    T getThenModify(UnaryOperator<T> operation) {
        T result = this.value;
        this.value = operation.apply(this.value);
        return result;
    }
    T modifyThenGet(UnaryOperator<T> operation) {
        this.value = operation.apply(this.value);
        return this.value;
    }
}