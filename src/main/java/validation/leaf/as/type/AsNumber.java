package validation.leaf.as.type;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.of.type.IsNumber;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

final public class AsNumber implements Validatable<Number>
{
    private Validatable<JsonElement> validatable;

    public AsNumber(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<Number> result() throws Throwable
    {
        Result<JsonElement> isNumberResult = new IsNumber(this.validatable).result();

        if (!isNumberResult.isSuccessful()) {
            return new FromNonSuccessful<>(isNumberResult);
        }

        if (!isNumberResult.value().isPresent()) {
            return new AbsentField<>(isNumberResult);
        }

        return new SuccessfulWithCustomValue<>(isNumberResult, this.value(isNumberResult));
    }

    private Number value(Result<JsonElement> result) throws Throwable
    {
        return result.value().raw().getAsNumber();
    }
}