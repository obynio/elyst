package com.sybiload.shopp;


public class Item
{
    private String name;
    private String description;
    private int icon;
    private boolean shop;
    private boolean done;

    public Item(String name, String footer, int icon, Boolean shop, Boolean done)
    {
        this.name = name;
        this.description = footer;
        this.icon = icon;
        this.shop = shop;
        this.done = done;
    }

    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return this.description;
    }
    public void setDescription (String description)
    {
        this.description = description;
    }

    public int getIcon()
    {
        return this.icon;
    }
    public void setIcon(int icon)
    {
        this.icon = icon;
    }

    public Boolean isToShop()
    {
        return this.shop;
    }
    public void toShop(Boolean shop)
    {
        this.shop = shop;
    }

    public Boolean isDone()
    {
        return this.done;
    }
    public void done(Boolean done)
    {
        this.done = done;
    }
}