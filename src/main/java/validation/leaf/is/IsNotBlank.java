package validation.leaf.is;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.as.AsString;
import validation.result.*;
import validation.value.Absent;
import validation.value.Present;
import validation.value.Value;

// todo: todoc: fields exists, but its value is blank. When value is absent, it means no such field exists
// todo: todoc: This one is used only with strings
final public class IsNotBlank implements Validatable<String>
{
    private Validatable<JsonElement> original;

    public IsNotBlank(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<String> result() throws Throwable
    {
        Result<String> prevResult = new AsString(this.original).result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return
                prevResult.isNamed()
                    ?
                    new Named<>(
                        prevResult.name(),
                        Either.right(
                            new Absent<>()
                        )
                    )
                    :
                    new Unnamed<>(
                        Either.right(
                            new Absent<>()
                        )
                    )
                ;
        }

        if (prevResult.value().raw().length() == 0) {
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

    private Either<Object, Value<String>> value(Result<String> prevResult) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    prevResult.value().raw()
                )
            );
    }

    private Either<Object, Value<String>> error()
    {
        return Either.left("This value must not be blank.");
    }
}
