package com.sybiload.elyst;


public class Item
{
    private String idItem;
    private String name;
    private String description;
    private int category;
    private double price;
    private double quantity;
    private int unit;
    private String barType;
    private String barCode;
    private boolean done;

    public Item(String idItem, String name, String description, int category, double price, double quantity,  int unit, String barType, String barCode, Boolean done)
    {
        this.idItem = idItem;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.barType = barType;
        this.barCode = barCode;
        this.done = done;
    }

    public String getIdItem()
    {
        return this.idItem;
    }
    public void setIdItem(String idItem)
    {
        this.idItem = idItem;
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

    public int getCategory()
    {
        return this.category;
    }
    public void setCategory(int category)
    {
        this.category = category;
    }

    public double getPrice()
    {
        return this.price;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getQuantity()
    {
        return this.quantity;
    }
    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }

    public int getUnit()
    {
        return this.unit;
    }
    public void setUnit (int unit)
    {
        this.unit = unit;
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

    public Boolean getDone()
    {
        return this.done;
    }
    public void setDone(Boolean done)
    {
        this.done = done;
    }
}