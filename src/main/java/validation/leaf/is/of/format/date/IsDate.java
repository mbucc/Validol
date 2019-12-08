package validation.leaf.is.of.format.date;

import validation.result.*;
import validation.Validatable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

final public class IsDate implements Validatable<String>
{
    private Validatable<String> original;
    private SimpleDateFormat format;

    public IsDate(Validatable<String> original, SimpleDateFormat format) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (format == null) {
            throw new Exception("Format can not be null");
        }

        this.original = original;
        this.format = format;
    }

    public Result<String> result() throws Exception
    {
        Result<String> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return prevResult;
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.isValidDate(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, new MustBeValidDate());
        }

        return prevResult;
    }

    private Boolean isValidDate(Result<String> prevResult) throws Exception
    {
        try {
            this.format.setLenient(false);
            this.format.parse(prevResult.value().raw().trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}