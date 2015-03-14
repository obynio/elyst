package com.sybiload.shopp.Adapter;

public class ListViewItem
{

    private String mail;
    private String title;

    public ListViewItem(String title, String mail)
    {
        super();
        this.mail = mail;
        this.title = title;
    }

    public ListViewItem(String title)
    {
        super();
        this.title = title;
    }

    public String getMail()
    {
        return mail;
    }

    public String getTitle()
    {
        return title;
    }
}