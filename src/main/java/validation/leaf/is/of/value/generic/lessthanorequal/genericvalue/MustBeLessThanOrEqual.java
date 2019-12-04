package validation.leaf.is.of.value.generic.lessthanorequal.genericvalue;

import validation.result.error.Error;

import java.util.Map;

public class MustBeLessThanOrEqual<T> implements Error
{
    private T value;

    public MustBeLessThanOrEqual(T value)
    {
        this.value = value;
    }

    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", new Code().value(),
                "message", new Message<>(this.value).value()
            );
    }
}
