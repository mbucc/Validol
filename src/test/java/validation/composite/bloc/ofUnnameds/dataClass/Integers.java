package validation.composite.bloc.ofUnnameds.dataClass;

import java.util.List;

public class Integers
{
    private List<Integer> list;

    public Integers(List<Integer> list)
    {
        this.list = list;
    }

    public List<Integer> list()
    {
        return this.list;
    }
}

