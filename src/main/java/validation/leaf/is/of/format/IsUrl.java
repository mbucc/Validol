package validation.leaf.is.of.format;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.apache.commons.validator.routines.EmailValidator;
import validation.Validatable;
import validation.leaf.as.AsString;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

import java.net.MalformedURLException;
import java.net.URL;

// TODO: Should "Is" validatables return Result<String> or Result<JsonElement>?
// Probably every time I use "Is" validatable that's implied to cast json type to a concrete, in reality I should've used an "As" validatable
final public class IsUrl implements Validatable<URL>
{
    private Validatable<JsonElement> original;

    public IsUrl(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<URL> result() throws Throwable
    {
        Result<String> result = new AsString(this.original).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        try {
            return
                result.isNamed()
                    ? new Named<>(result.name(), this.value(result))
                    : new Unnamed<>(this.value(result))
                ;
        } catch (MalformedURLException e) {
            return new NonSuccessfulWithCustomError<>(result, this.error().getLeft());
        }
    }

    private Either<Object, Value<URL>> value(Result<String> result) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    new URL(result.value().raw())
                )
            );
    }

    private Either<Object, Value<JsonElement>> error()
    {
        return Either.left("This value must be a valid url.");
    }
}