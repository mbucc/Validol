package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.AbsentField;
import validation.result.FromNonSuccessful;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import validation.value.Value;

final public class IsNotEqualTo<T> implements Validatable<T>
{
    private Validatable<T> original;
    private T value;

    public IsNotEqualTo(Validatable<T> original, T value)
    {
        this.original = original;
        this.value = value;
    }

    public Result<T> result() throws Throwable
    {
        Result<T> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (prevResult.value().raw().equals(this.value)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return prevResult;
    }

    private Either<Object, Value<T>> error()
    {
        return Either.left(String.format("This value must not be equal to %s.", this.value));
    }
}