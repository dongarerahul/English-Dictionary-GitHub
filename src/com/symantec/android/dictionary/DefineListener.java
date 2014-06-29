package com.symantec.android.dictionary;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DefineListener extends AbstractListener implements OnClickListener {
	
	public DefineListener(Activity activity) {
		super(activity);
	}

	@Override
	public void onClick(View view) {

		if(view.getId() != R.id.defineButton) return;
		
		String word = helper.getText(R.id.wordText);
		String definition = helper.getText(R.id.definitionText);
		
		if((validate(definition) && validate(word)) == false) {
			sendMessage(view, R.string.error_define_validate);
			return;
		}
		
		Uri uri = model.addDefinition(word, definition);
		int messageId = uri == null ? R.string.error_add_definition : R.string.success_add_definition;
		
		showDefinitions();
		sendMessage(view, messageId);
	}

	// Show Definitions by reusing lookup feature
	private void showDefinitions() {
		Button lookup = helper.getButton(R.id.lookupButton);
		OnClickListener handler = new LookupListener(activity);
		handler.onClick(lookup);
	}
}
