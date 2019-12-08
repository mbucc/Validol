package validation.leaf.is.of.format.url;

import validation.Validatable;
import validation.result.*;

import java.net.MalformedURLException;
import java.net.URL;

final public class IsUrl implements Validatable<String>
{
    private Validatable<String> original;

    public IsUrl(Validatable<String> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
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

        if (!this.isValidUrl(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, new MustBeValidUrl());
        }

        return prevResult;
    }

    private Boolean isValidUrl(Result<String> result) throws Exception
    {
        try {
            new URL(result.value().raw());
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}