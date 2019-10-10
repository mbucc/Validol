package validation.leaf.as.type;

import validation.leaf.is.of.type.IsInteger;
import validation.result.*;
import validation.Validatable;
import validation.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.value.Value;

final public class AsInteger implements Validatable<Integer>
{
    private Validatable<JsonElement> validatable;

    public AsInteger(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<Integer> result() throws Throwable
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!new IsInteger(this.validatable).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(result, "This value must be an integer.");
        }

        return new SuccessfulWithCustomValue<>(result, this.value(result));
    }

    private Integer value(Result<JsonElement> result) throws Throwable
    {
        return Integer.parseInt(result.value().raw().toString());
    }
}