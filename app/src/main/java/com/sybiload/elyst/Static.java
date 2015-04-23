package com.sybiload.elyst;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Static
{
    public static ArrayList<List> allList = new ArrayList<List>();
    public static List currentList;
    public static List widgetList;

    public static int[] cardDrw = {
            R.drawable.bg_food,
            R.drawable.bg_bakery,
            R.drawable.bg_butchery,
            R.drawable.bg_fish,
            R.drawable.bg_spice,
            R.drawable.bg_medicine,
            R.drawable.bg_library,
            R.drawable.bg_diy,
            R.drawable.bg_beauty};
}
