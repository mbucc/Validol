package validation.composite.bloc.ofUnnameds;

import validation.composite.bloc.ofNameds.UnnamedBlocOfNameds;
import validation.composite.bloc.ofUnnameds.dataClass.Integers;
import validation.composite.bloc.ofUnnameds.dataClass.Item;
import validation.composite.bloc.ofUnnameds.dataClass.Items;
import validation.leaf.as.AsInteger;
import validation.leaf.is.IsInteger;
import validation.leaf.Unnamed;
import validation.result.Result;
import validation.value.Present;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Ignore;
import org.junit.Test;
import validation.leaf.Named;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class UnnamedBlocOfUnnamedsTest
{
    @Test
    public void successfulValidationOfPlainUnnamed() throws Throwable
    {
        Result<Integers> result =
            new UnnamedBlocOfUnnameds<>(
                this.jsonArrayOfIntegers(),
                jsonElement ->
                    new AsInteger(
                        new Unnamed<>(
                            Either.right(
                                new Present<>(
                                    jsonElement
                                )
                            )
                        )
                    ),
                Integers.class
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(Integer.valueOf(1900), result.value().raw().list().get(0));
        assertEquals(Integer.valueOf(777), result.value().raw().list().get(1));
    }

    @Test
    public void failedValidationOfPlainUnnameds() throws Throwable
    {
        Result<Integers> result =
            new UnnamedBlocOfUnnameds<>(
                this.jsonArrayOfStrings(),
                jsonElement ->
                    new IsInteger(
                        new Unnamed<>(
                            Either.right(
                                new Present<>(
                                    jsonElement
                                )
                            )
                        )
                    ),
                Integers.class
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(List.of("This value must be an integer.", "This value must be an integer."), result.error());
    }

    @Test
    public void successfulValidationOfUnnamedBlocks() throws Throwable
    {
        Result<Items> result =
            new UnnamedBlocOfUnnameds<>(
                this.jsonArrayOfMaps(),
                jsonMapElement ->
                    new UnnamedBlocOfNameds<>(
                        List.of(
                            new Named<>(
                                "id",
                                Either.right(
                                    new Present<>(jsonMapElement.getAsJsonObject().get("id").getAsInt())
                                )
                            )
                        ),
                        Item.class
                    ),
                Items.class
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(Integer.valueOf(1900), result.value().raw().list().get(0).id());
        assertEquals(Integer.valueOf(777), result.value().raw().list().get(1).id());
    }

    @Test
    public void fail() throws Throwable
    {
        Result<Items> result =
            new UnnamedBlocOfUnnameds<>(
                this.jsonArrayOfMaps(),
                jsonMapElement ->
                    new UnnamedBlocOfNameds<>(
                        List.of(
                            new Named<>(
                                "id",
                                Either.left("Wooooooops")
                            )
                        ),
                        Item.class
                    ),
                Items.class
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(
            List.of(
                Map.of("id", "Wooooooops"),
                Map.of("id", "Wooooooops")
            ),
            result.error()
        );
    }

    @Test
    public void wrongStructure() throws Throwable
    {
        Result<Items> result =
            new UnnamedBlocOfUnnameds<>(
                this.messyJson(),
                jsonMapElement ->
                    new UnnamedBlocOfNameds<>(
                        List.of(
                            new Named<>(
                                "id",
                                Either.left("Wooooooops")
                            )
                        ),
                        Item.class
                    ),
                Items.class
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(
            "This block must be an array.",
            result.error()
        );
    }

    private JsonElement jsonArrayOfMaps()
    {
        return
            new Gson().toJsonTree(
                List.of(
                    Map.of("id", 1900),
                    Map.of("id", 777)
                ),
                new TypeToken<List<Map<String, Object>>>() {}.getType()
            );
    }

    private JsonElement jsonArrayOfIntegers()
    {
        return
            new Gson().toJsonTree(
                List.of(1900, 777),
                new TypeToken<List<Integer>>() {}.getType()
            );
    }

    private JsonElement jsonArrayOfStrings()
    {
        return
            new Gson().toJsonTree(
                List.of("vasya", "fedya"),
                new TypeToken<List<Integer>>() {}.getType()
            );
    }

    private JsonElement messyJson()
    {
        return
            new Gson().toJsonTree(
                Map.of(
                    "id", 777,
                    "vasya", "belov"
                ),
                new TypeToken<Object>() {}.getType()
            );
    }
}
