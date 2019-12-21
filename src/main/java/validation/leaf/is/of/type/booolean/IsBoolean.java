package validation.leaf.is.of.type.booolean;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.structure.jsonprimitive.IsJsonPrimitive;
import validation.leaf.is.of.structure.jsonprimitive.MustBeJsonPrimitive;
import validation.result.*;
import validation.result.error.Error;

final public class IsBoolean implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;
    private Error error;

    public IsBoolean(Validatable<JsonElement> original, Error error) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.original = original;
        this.error = error;
    }

    public IsBoolean(Validatable<JsonElement> original) throws Exception
    {
        this(original, new MustBeBoolean());
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<JsonElement> prevResult = new IsJsonPrimitive(this.original, this.error).result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.isBoolean(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
        }

        return new SuccessfulWithCustomValue<>(prevResult, prevResult.value().raw());
    }

    private Boolean isBoolean(Result<JsonElement> prevResult) throws Exception
    {
        return
            prevResult.value().raw().toString()
                .equals(
                    String.valueOf(prevResult.value().raw().getAsBoolean())
                );
    }
}
