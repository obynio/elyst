package com.sybiload.shopp;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class List
{
    private String name;
    private String description;
    private String database;
    private ArrayList<Item> item;

    public List(String name, String description, String database)
    {
        this.name = name;
        this.description = description;
        this.database = database;
        item = new ArrayList<Item>();

        // sort all this stuff
        new Misc().sortItem(this.item);
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
        this.description= description;
    }

    public String getDatabase()
    {
        return this.database;
    }
    public void setDatabase(String database)
    {
        this.database = database;
    }

    public ArrayList<Item> getItem()
    {
        return this.item;
    }
    public void setItem(ArrayList<Item> item)
    {
        this.item = item;
        new Misc().sortItem(this.item);
    }
}