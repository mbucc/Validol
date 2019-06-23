package Validation.Composite;

import java.util.List;

class Items
{
    private List<Item> list;

    public Items(List<Item> list)
    {
        this.list = list;
    }

    public List<Item> list()
    {
        return this.list;
    }
}
