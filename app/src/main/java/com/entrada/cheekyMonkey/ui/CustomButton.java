package com.entrada.cheekyMonkey.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.entrada.cheekyMonkey.R;

public class CustomButton extends Button {

    private static final String HelveticaNeueLTProBd = "HelveticaNeueLTPro-Bd";
    private static final String HelveticaNeueLTProRoman = "HelveticaNeueLTPro-Roman";
    private static final String Roboto_Black = "Roboto-Black";
    private static final String Roboto_BlackItalic = "Roboto-BlackItalic";
    private static final String Roboto_Thin = "Roboto-Thin";
    private static final String Roboto_Medium = "Roboto-Medium";

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode())
            init(attrs);

    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode())
            init(attrs);

    }

    public CustomButton(Context context) {
        super(context);

        if (!isInEditMode())
            init(null);

    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CustomButton);
            String fontName = a
                    .getString(R.styleable.CustomButton_custombuttonfont);
            if (HelveticaNeueLTProBd.equals(fontName)) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), "fonts/HelveticaNeueLTPro-Bd.otf");
                setTypeface(myTypeface);
            }
            if (HelveticaNeueLTProRoman.equals(fontName)) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), "fonts/HelveticaNeueLTPro-Roman.otf");
                setTypeface(myTypeface);
            }
            if (Roboto_Black.equals(fontName)) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), "fonts/Roboto-Black.ttf");
                setTypeface(myTypeface);
            }
            if (Roboto_BlackItalic.equals(fontName)) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), "fonts/Roboto-BlackItalic.ttf");
                setTypeface(myTypeface);
            }
            if (Roboto_Thin.equals(fontName)) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), "fonts/Roboto-Thin.ttf");
                setTypeface(myTypeface);
            }
            if (Roboto_Medium.equals(fontName)) {
                Typeface myTypeface = Typeface.createFromAsset(getContext()
                        .getAssets(), "fonts/Roboto-Medium.ttf");
                setTypeface(myTypeface);
            }

            a.recycle();
        }

    }
}
