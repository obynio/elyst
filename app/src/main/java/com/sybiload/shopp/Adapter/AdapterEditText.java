package com.sybiload.shopp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

public class AdapterEditText extends EditText
{
    public AdapterEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void setError(CharSequence error, Drawable icon)
    {
        setCompoundDrawables(null, null, icon, null);
    }
}
