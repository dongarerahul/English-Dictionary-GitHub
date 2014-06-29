package com.symantec.android.dictionary.utilties;

import com.symantec.android.dictionary.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ActivityHelper {

	private Activity activity;

	public ActivityHelper(Activity activity) {
		this.activity = activity;
	}

	public void clear(int textField) {
		getTextField(textField).getText().clear();
	}

	public String getText(int textFieldId) {
		EditText wordText = getTextField(textFieldId);
		String word = wordText.getText().toString();
		return word == null ? null : word.trim();
	}

	public EditText getTextField(int textFieldId) {
		return (EditText) activity.findViewById(textFieldId);
	}
	
	public Button getButton(int buttonId) {
		return (Button)activity.findViewById(buttonId);
	}

	public LinearLayout getLayout(int layoutId) {
		return (LinearLayout) activity.findViewById(layoutId);
	}

	public void closeKeyboard(View view) {
		InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	public void addTextView(int layoutId, String definition) {
		TextView text = new TextView(activity);
		text.setSingleLine(false);
		text.setText(definition);
		text.setTextAppearance(activity.getApplicationContext(), android.R.attr.textAppearanceLarge);
		
		LinearLayout layout = getLayout(layoutId);
		LayoutParams parameters = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		layout.addView(text, 0, parameters);// always add at top
	}

	public void clearLayout(int definitionlayout) {
		getLayout(R.id.definitionLayout).removeAllViews();
	}
}
