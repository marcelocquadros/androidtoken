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

public class PinRemove extends Activity {

	private static final int DIALOG_INVALID_PIN = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinremove);
		
		Button removePinBtn = (Button)findViewById(R.id.pinRemoveSubmit);
		removePinBtn.setOnClickListener(removeBtn);
	}
	
	private OnClickListener removeBtn = new OnClickListener() {
		
		public void onClick(View v) {
			//valid the pin
			
			String pin = ((EditText)findViewById(R.id.pinRemoveExistingPinEdit)).getText().toString();
			
			if(PinManager.validatePin(v.getContext(), pin)){
				PinManager.removePin(v.getContext());
				finish();
			}else{
				// the pin isn't the same as the one stored, do nothing
				showDialog(DIALOG_INVALID_PIN);
				return;
			}
			
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d;
		
		switch(id){
		
		case DIALOG_INVALID_PIN:
			d = createAlertDialog(R.string.pinAlertInvalidPin);
			break;
			
			default:
				d = null;
				break;
		
		}		
		
		return d;
	}
	
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
