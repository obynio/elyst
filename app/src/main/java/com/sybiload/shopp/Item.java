package com.sybiload.shopp;


public class Item
{
    private String name;
    private String description;
    private int icon;
    private String barType;
    private String barCode;
    private boolean shop;
    private boolean done;

    public Item(String name, String footer, int icon, String barType, String barCode, Boolean shop, Boolean done)
    {
        this.name = name;
        this.description = footer;
        this.icon = icon;
        this.barType = barType;
        this.barCode = barCode;
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

    public String getBarType()
    {
        return this.barType;
    }
    public void setBarType(String barType)
    {
        this.barType = barType;
    }

    public String getBarCode()
    {
        return this.barCode;
    }
    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
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