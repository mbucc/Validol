package validation.leaf.is;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.*;
import validation.value.Absent;
import validation.value.Present;
import validation.value.Value;

// todo: todoc: fields exists, but its value is blank. When value is absent, it means no such field exists
final public class IsBlank implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsBlank(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), Either.right(new Absent<>()))
                    : new Unnamed<>(Either.right(new Absent<>()))
                ;
        }

        if (!new IsJsonPrimitive(this.original).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error());
        }

        if (!prevResult.value().raw().getAsJsonPrimitive().isJsonNull()) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error());
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

    private String error()
    {
        return "This value must be blank.";
    }
}
