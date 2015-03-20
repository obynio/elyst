package com.sybiload.shopp.Adapter;

import android.graphics.drawable.Drawable;

public class ListViewItem
{

    private String mail;
    private String title;
    private Drawable icon;

    public ListViewItem(String title, String mail)
    {
        super();
        this.mail = mail;
        this.title = title;
    }

    public ListViewItem(String title, Drawable icon)
    {
        super();
        this.title = title;
        this.icon = icon;
    }

    public String getMail()
    {
        return mail;
    }

    public String getTitle()
    {
        return title;
    }

    public Drawable getIcon()
    {
        return icon;
    }
}