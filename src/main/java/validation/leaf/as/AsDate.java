package validation.leaf.as;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import validation.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final public class AsDate implements Validatable<Date>
{
    private Validatable<JsonElement> original;
    private SimpleDateFormat format;

    public AsDate(Validatable<JsonElement> original, SimpleDateFormat format)
    {
        this.original = original;
        this.format = format;
    }

    // TODO: IsLeaf and IsArray, IsJsonObject? Three classes can be used to check the structure. After that, Is<Something> and As<Something>.
    // TODO: todoc: Sometimes one need just to make sure that underlying data is of a specific type,without casting it. That's when Is... validatables come in handy.
    public Result<Date> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new Named<>(prevResult.name(), Either.left(prevResult.error()));
        }

        if (!prevResult.value().raw().isJsonPrimitive()) {
            throw new Exception("Use IsDate validatable to make sure that underlying raw is a date");
        }

        try {
            this.format.setLenient(false);

            return
                new Named<>(
                    prevResult.name(),
                    Either.right(
                        new Present<>(
                            this.format.parse(prevResult.value().raw().getAsString().trim())
                        )
                    )
                );
        } catch (ParseException e) {
            return new Named<>(prevResult.name(), Either.left("This element should represent a date"));
        }
    }
}
