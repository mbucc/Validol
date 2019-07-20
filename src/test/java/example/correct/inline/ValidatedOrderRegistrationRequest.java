package example.correct.inline;

import example.correct.bag.delivery.courier.CourierDelivery;
import example.correct.bag.delivery.courier.when.DefaultWhenData;
import example.correct.bag.delivery.courier.where.Where;
import example.correct.bag.guest.Guest;
import example.correct.bag.items.item.Item;
import example.correct.bag.items.Items;
import example.correct.bag.OrderRegistrationRequestData;
import validation.composite.*;
import validation.composite.switcz.Specific;
import validation.composite.switcz.SwitchTrue;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.bloc.of.unnameds.NamedBlocOfUnnameds;
import validation.leaf.as.AsDate;
import validation.leaf.as.AsInteger;
import validation.leaf.as.AsString;
import validation.leaf.IndexedValue;
import validation.leaf.is.IsJsonObject;
import validation.result.Result;
import validation.Validatable;
import validation.value.Present;
import com.spencerwi.either.Either;
import validation.leaf.Named;
import validation.leaf.Required;

import java.text.SimpleDateFormat;
import java.util.List;

// TODO: 7/1/19 Create another class representing the same validatable request, but with nested blocks. That would be a more concise version with improved readability.
public class ValidatedOrderRegistrationRequest implements Validatable<OrderRegistrationRequestData>
{
    private String jsonRequestString;

    public ValidatedOrderRegistrationRequest(String jsonRequestString)
    {
        this.jsonRequestString = jsonRequestString;
    }

    @Override
    public Result<OrderRegistrationRequestData> result() throws Throwable
    {
        // todo: Обработать случаи, когда классы используются неверно. Например, NamedBlocOfNameds используется с неименованными элементами валидации.
        return
            new FastFail<>(
                new WellFormedJson(
                    new Named<>("parsed request body", Either.right(new Present<>(this.jsonRequestString)))
                ),
                requestJsonObject ->
                    new NamedBlocOfNameds<>(
                        "parsed request body",
                        List.of(
                            new FastFail<>(
                                new IsJsonObject(
                                    new Required(
                                        new IndexedValue("guest", requestJsonObject)
                                    )
                                ),
                                guestJsonObject ->
                                    new NamedBlocOfNameds<>(
                                        "guest",
                                        List.of(
                                            new AsString(
                                                new Required(
                                                    new IndexedValue("email", guestJsonObject)
                                                )
                                            ),
                                            new AsString(
                                                new Required(
                                                    new IndexedValue("name", guestJsonObject)
                                                )
                                            )
                                        ),
                                        Guest.class
                                    )
                            ),
                            new FastFail<>(
                                new Required(
                                    new IndexedValue("items", requestJsonObject)
                                )
                                ,
                                itemsJsonElement ->
                                    new NamedBlocOfUnnameds<>(
                                        "items",
                                        itemsJsonElement,
                                        item ->
                                            new UnnamedBlocOfNameds<>(
                                                List.of(
                                                    new AsInteger(
                                                        new Required(
                                                            new IndexedValue("id", item)
                                                        )
                                                    )
                                                ),
                                                Item.class
                                            ),
                                        Items.class
                                    )
                            ),
                            new FastFail<>(
                                new Required(
                                    new IndexedValue("delivery", requestJsonObject)
                                ),
                                deliveryJsonElement ->
                                    new SwitchTrue<>(
                                        "delivery",
                                        List.of(
                                            new Specific<>(
                                                () -> true,
                                                new UnnamedBlocOfNameds<>(
                                                    List.of(
                                                        new FastFail<>(
                                                            new IndexedValue("where", deliveryJsonElement),
                                                            whereJsonElement ->
                                                                new NamedBlocOfNameds<>(
                                                                    "where",
                                                                    List.of(
                                                                        new AsString(
                                                                            new Required(
                                                                                new IndexedValue("street", whereJsonElement)
                                                                            )
                                                                        ),
                                                                        new AsInteger(
                                                                            new Required(
                                                                                new IndexedValue("building", whereJsonElement)
                                                                            )
                                                                        )
                                                                    ),
                                                                    Where.class
                                                                )
                                                        ),
                                                        new FastFail<>(
                                                            new IndexedValue("when", deliveryJsonElement),
                                                            whenJsonElement ->
                                                                new NamedBlocOfNameds<>(
                                                                    "when",
                                                                    List.of(
                                                                        new AsDate(
                                                                            new Required(
                                                                                new IndexedValue("date", whenJsonElement)
                                                                            ),
                                                                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                                        )
                                                                    ),
                                                                    DefaultWhenData.class
                                                                )
                                                        )
                                                    ),
                                                    CourierDelivery.class
                                                )
                                            )
                                        )
                                    )
                            ),
                            new AsInteger(
                                new Required(
                                    new IndexedValue("source", requestJsonObject)
                                )
                            )
                        ),
                        OrderRegistrationRequestData.class
                    )
            )
                .result()
            ;
    }
}