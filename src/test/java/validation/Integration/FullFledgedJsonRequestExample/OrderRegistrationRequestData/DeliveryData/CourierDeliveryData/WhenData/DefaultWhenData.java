package validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.WhenData;

import validation.value.Present;
import validation.value.Value;

import java.util.Date;

public class DefaultWhenData implements WhenData
{
    private Date date;

    public DefaultWhenData(Date date)
    {
        this.date = date;
    }

    public Value<Date> date()
    {
        return new Present<>(this.date);
    }
}
