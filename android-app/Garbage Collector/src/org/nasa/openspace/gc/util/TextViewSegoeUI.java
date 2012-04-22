package org.nasa.openspace.gc.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewSegoeUI extends TextView {
	public TextViewSegoeUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		
			  if (!isInEditMode()) {
				 Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeuil.ttf");
		         setTypeface(tf);
			  }
		
	}

}
