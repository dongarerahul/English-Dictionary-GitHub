package com.company.android.dictionary;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class LookupListener extends AbstractListener implements OnClickListener {

	public LookupListener(Activity activity) {
		super(activity);
	}

	@Override
	public void onClick(View view) {
		
		if(view.getId() != R.id.lookupButton) return;
		
		String word = validateInput(R.id.wordText);
		if(word == null) {
			sendMessage(view, R.string.error_word);
			return;
		}
		
		clearViews();
		
		List<String> definitions = model.lookup(word);
		populate(view, definitions);
		
		helper.closeKeyboard(view);
	}

	private void clearViews() {
		helper.clearLayout(R.id.definitionLayout);
		helper.clear(R.id.definitionText);
	}

	private String validateInput(int viewId) {
		EditText field = (EditText)helper.getTextField(viewId);
		String text = field.getText().toString();
		
		return validate(text) ? text.trim() : null;
	}

	private void populate(View view, List<String> definitions) {
		if(definitions.isEmpty()) {
			sendMessage(view, R.string.error_lookup);
			return;
		}
		
		for(String definition : definitions) { 
			helper.addTextView(R.id.definitionLayout, definition);
		} 
	}
}
