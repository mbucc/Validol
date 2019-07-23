package example.correct.split;

import com.spencerwi.either.Either;
import example.correct.bag.OrderRegistrationRequestData;
import validation.composite.bloc.of.callback.RequiredNamedBlocOfCallback;
import example.correct.split.delivery.courier.Courier;
import example.correct.split.guest.Guest;
import example.correct.split.items.Items;
import example.correct.split.source.Source;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.WellFormedJson;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.composite.switcz.Specific;
import validation.composite.switcz.SwitchTrue;
import validation.leaf.Named;
import validation.result.Result;
import validation.value.Present;
import java.util.List;

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
        return
            new FastFail<>(
                new WellFormedJson(
                    new Named<>("parsed request body", Either.right(new Present<>(this.jsonRequestString)))
                ),
                requestJsonObject ->
                    new NamedBlocOfNameds<>(
                        "parsed request body",
                        List.of(
                            new Guest(requestJsonObject),
                            new Items(requestJsonObject),
                            new RequiredNamedBlocOfCallback<>(
                                "delivery",
                                requestJsonObject,
                                deliveryJsonElement ->
                                    new SwitchTrue<>(
                                        "delivery",
                                        List.of(
                                            new Specific<>(
                                                // todo: add real condition
                                                () -> true,
                                                new Courier(deliveryJsonElement)
                                            )
                                        )
                                    )
                            ),
                            new Source(requestJsonObject)
                        ),
                        OrderRegistrationRequestData.class
                    )
            )
                .result()
            ;
    }
}
