package validation.leaf.as.format;

import validation.Validatable;
import validation.leaf.is.of.format.url.IsUrl;
import validation.result.*;
import java.net.URL;

final public class AsUrl implements Validatable<URL>
{
    private Validatable<String> original;

    public AsUrl(Validatable<String> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<URL> result() throws Exception
    {
        Result<String> isUrlResult = new IsUrl(this.original).result();

        if (!isUrlResult.isSuccessful()) {
            return new FromNonSuccessful<>(isUrlResult);
        }

        if (!isUrlResult.value().isPresent()) {
            return new AbsentField<>(isUrlResult);
        }

        return new SuccessfulWithCustomValue<>(isUrlResult, new URL(isUrlResult.value().raw()));
    }
}
