package com.jaguarlabs.sipaccel.util;

import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class SearchClearButtonTextWatcher extends ClearButtonTextWatcher {

	ISearchHandler iSearch;
	ImageButton toggleButton;
	
	public SearchClearButtonTextWatcher(ImageButton pButton, AutoCompleteTextView pTextField, 
			ISearchHandler pISearch, ImageButton pToggle) {
		super(pButton, pTextField);
		iSearch = pISearch;
		toggleButton = pToggle;
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		super.onTextChanged(s, start, before, count);
		iSearch.doSearch( s );
	}

}
