package validation.leaf.is.of.value.lessthan.zero;

import validation.Validatable;
import validation.result.*;

// doc: all validatables are successful if the corresponding field is absent.
// If you want it to be required, specify it explicitly.
final public class IsNegative implements Validatable<Number>
{
    private Validatable<Number> original;

    public IsNegative(Validatable<Number> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<Number> result() throws Exception
    {
        Result<Number> asNumber = this.original.result();

        if (!asNumber.isSuccessful() || !asNumber.value().isPresent()) {
            return asNumber;
        }

        if (asNumber.value().raw().doubleValue() >= 0) {
            return new NonSuccessfulWithCustomError<>(asNumber, new MustBeNegative());
        }

        return new SuccessfulWithCustomValue<>(asNumber, asNumber.value().raw());
    }
}