package Validation.Result;

import Validation.Value.Present;
import Validation.Value.Value;
import com.spencerwi.either.Either;

public class Unnamed<T> implements Result<T>
{
    private Either<Object, Value<T>> value;

    public Unnamed(Either<Object, Value<T>> value)
    {
        this.value = value;
    }

    @Override
    public Boolean isSuccessful()
    {
        return this.value.isRight();
    }

    @Override
    public Value<T> value() throws Exception
    {
        if (!this.isSuccessful()) {
            throw new Exception("No raw exists on a non-successful element");
        }

        return this.value.getRight();
    }

    @Override
    public Object error() throws Exception {
        if (this.isSuccessful()) {
            throw new Exception("No error exists on a successful element");
        }

        return this.value.getLeft();
    }

    @Override
    public Boolean isNamed()
    {
        return false;
    }

    @Override
    public String name() throws Exception
    {
        throw new Exception("Unnamed result does not have a name");
    }
}
