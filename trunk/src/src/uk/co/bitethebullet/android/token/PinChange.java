package uk.co.bitethebullet.android.token;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PinChange extends Activity {

	private static final int DIALOG_INVALID_EXISTING_PIN = 0;
	private static final int DIALOG_DIFF_NEW_PIN = 1;
	
	Boolean hasExistingPin = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinchange);
		
		if(PinManager.hasPinDefined(this)){
			hasExistingPin = false;
			EditText existPinEdit = (EditText)findViewById(R.id.pinChangeExistingPinEdit);
			existPinEdit.setEnabled(false);
		}
		
		Button submitBtn = (Button)findViewById(R.id.pinChangeSubmit);
		submitBtn.setOnClickListener(submitClick);		
	}
	
	private OnClickListener submitClick = new 	OnClickListener() {
		
		public void onClick(View v) {
			//validate the existing pin
			if(hasExistingPin){
				
			}
			
			//validate the two new pins match
			
			//store
		}
	};
	
	private Dialog createAlertDialog(int messageId){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(messageId)
			   .setCancelable(false)
			   .setPositiveButton(R.string.dialogPositive, dialogClose);
		
		return builder.create();
		
	}
	
	private DialogInterface.OnClickListener dialogClose = new 	DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};

}
