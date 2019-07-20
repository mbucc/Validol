package validation.composite.bloc.of.unnamed;

import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.bloc.of.unnameds.NamedBlocOfUnnameds;
import validation.composite.bloc.of.unnamed.bag.Item;
import validation.composite.bloc.of.unnamed.bag.Items;
import validation.leaf.Named;
import validation.result.Result;
import validation.value.Present;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class NamedBlocOfUnnamedsTest
{
    @Test
    public void success() throws Throwable
    {
        Result<Items> result =
            new NamedBlocOfUnnameds<>(
                "items",
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
            new NamedBlocOfUnnameds<>(
                "items",
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
            new NamedBlocOfUnnameds<>(
                "items",
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

