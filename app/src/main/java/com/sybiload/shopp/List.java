package com.sybiload.shopp;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class List
{
    private String name;
    private String description;
    private String database;
    public static ArrayList<Item> itemShop;
    public static ArrayList<Item> itemAvailable;

    public List(String name, String description, String database)
    {
        this.name = name;
        this.description = description;
        this.database = database;
        itemShop = new ArrayList<Item>();
        itemAvailable = new ArrayList<Item>();
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


    // sort item in alphabetical order
    public void sortShop()
    {
        Collections.sort(itemShop, new Comparator<Item>()
        {
            public int compare(Item v1, Item v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
    }

    // sort item in alphabetical order
    public void sortAvailable()
    {
        Collections.sort(itemAvailable, new Comparator<Item>()
        {
            public int compare(Item v1, Item v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
    }

    // sort item in alphabetical order
    public void sortShopDone()
    {
        Collections.sort(itemShop, new Comparator<Item>()
        {
            public int compare(Item v1, Item v2) {
                return v1.isDone().compareTo(v2.isDone());
            }
        });
    }
}