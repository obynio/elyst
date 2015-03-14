package com.sybiload.shopp;

public class Item
{
    private String myItem;

    public Item(String thing)
    {
        myItem = thing;
    }

    public String getName()
    {
        return myItem;
    }

    public void setName(String name)
    {
        myItem = name;
    }
}
