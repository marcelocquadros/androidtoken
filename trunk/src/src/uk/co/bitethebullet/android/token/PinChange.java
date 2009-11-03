package uk.co.bitethebullet.android.token;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PinChange extends Activity {

	private static final int DIALOG_INVALID_EXISTING_PIN = 0;
	private static final int DIALOG_DIFF_NEW_PIN = 1;
	private static final int DIALOG_NO_NEW_PIN = 2;
	
	Boolean hasExistingPin = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinchange);
		
		if(!PinManager.hasPinDefined(this)){
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
				
				String existingPin = ((EditText)findViewById(R.id.pinChangeExistingPinEdit)).getText().toString();
				
				if(!PinManager.validatePin(v.getContext(), existingPin)){
					//the pin entered is not the one stored, show
					//warning and stop					
					showDialog(DIALOG_INVALID_EXISTING_PIN);
					return;
				}
			}
			
			//validate the two new pins match
			String newPin1 = ((EditText)findViewById(R.id.pinChangeNew1Edit)).getText().toString();
			String newPin2 = ((EditText)findViewById(R.id.pinChangeNew2Edit)).getText().toString();
			
			if(newPin1.length() == 0){
				showDialog(DIALOG_NO_NEW_PIN);
				return;
			}
			
			if(!newPin1.contentEquals(newPin2)){
				showDialog(DIALOG_DIFF_NEW_PIN);
				return;
			}
			
			//store
			PinManager.storePin(v.getContext(), newPin1);
			finish();
		}
	};
	
	private Dialog createAlertDialog(int messageId){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(messageId)
			   .setCancelable(false)
			   .setPositiveButton(R.string.dialogPositive, dialogClose);
		
		return builder.create();
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d;
		
		switch(id){
		case DIALOG_DIFF_NEW_PIN:
			d = createAlertDialog(R.string.pinAlertNewPinsDifferent);
			break;
			
		case DIALOG_INVALID_EXISTING_PIN:
			d = createAlertDialog(R.string.pinAlertInvalidPin);
			break;
			
		case DIALOG_NO_NEW_PIN:
			d = createAlertDialog(R.string.pinAlertNewPinBlank);
			break;
			
		default:
			d = null;
		}
		
		return d;
	}

	private DialogInterface.OnClickListener dialogClose = new 	DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};

}
