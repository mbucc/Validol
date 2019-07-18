package validation.leaf.is;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.Named;
import validation.result.Result;
import validation.result.Unnamed;
import validation.value.Absent;
import validation.value.Present;
import validation.value.Value;

final public class IsString implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsString(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), Either.left(prevResult.error()))
                    : new Unnamed<>(Either.left(prevResult.error()))
                ;
        }

        if (!prevResult.value().isPresent()) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), Either.right(new Absent<>()))
                    : new Unnamed<>(Either.right(new Absent<>()))
                ;
        }

        if (!new IsJsonPrimitive(this.original).result().isSuccessful()) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), this.error())
                    : new Unnamed<>(this.error())
                ;
        }

        if (!prevResult.value().raw().getAsJsonPrimitive().isString()) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), this.error())
                    : new Unnamed<>(this.error())
                ;
        }

        return
            prevResult.isNamed()
                ? new Named<>(prevResult.name(), this.value(prevResult))
                : new Unnamed<>(this.value(prevResult))
            ;
    }

    private Either<Object, Value<JsonElement>> value(Result<JsonElement> prevResult) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    prevResult.value().raw()
                )
            );
    }

    private Either<Object, Value<JsonElement>> error()
    {
        return Either.left("This value must be a string.");
    }
}
