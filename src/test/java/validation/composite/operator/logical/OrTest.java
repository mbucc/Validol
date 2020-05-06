package validation.composite.operator.logical;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.composite.operator.logical.or.EitherLeftOrRight;
import validation.composite.operator.logical.or.Or;
import validation.leaf.is.of.type.integer.MustBeInteger;
import validation.leaf.is.of.type.string.MustBeString;
import validation.result.Result;
import validation.result.Unnamed;
import validation.result.value.Present;

import java.util.Map;

import static org.junit.Assert.*;

final public class OrTest
{
    @Test
    public void testSuccessful() throws Exception
    {
        JsonElement json =
            new Gson().toJsonTree(
                Map.of(
                    "header", 124
                )
            );

        assertTrue(
            (new Or(
                () -> new Unnamed<>(Either.right(new Present<>(true))),
                () -> new Unnamed<>(Either.right(new Present<>(true)))
            ))
                .result()
                    .isSuccessful()
        );
    }

    @Test
    public void testFirstOneFailed() throws Exception
    {
        assertTrue(
            (new Or(
                () -> new Unnamed<>(Either.left(new MustBeInteger())),
                () -> new Unnamed<>(Either.right(new Present<>(true)))
            ))
                .result()
                    .isSuccessful()
        );
    }

    @Test
    public void testSecondOneFailed() throws Exception
    {
        assertTrue(
            (new Or(
                () -> new Unnamed<>(Either.right(new Present<>(true))),
                () -> new Unnamed<>(Either.left(new MustBeInteger()))
            ))
                .result()
                    .isSuccessful()
        );
    }

    @Test
    public void testBothFailed() throws Exception
    {
        Result<Boolean> result =
            (new Or(
                () -> new Unnamed<>(Either.left(new MustBeInteger())),
                () -> new Unnamed<>(Either.left(new MustBeString()))
            ))
                .result();

        assertFalse(result.isSuccessful());
    }
}
