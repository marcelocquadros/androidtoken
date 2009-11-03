package uk.co.bitethebullet.android.token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class TokenAdd extends Activity {

	private static final int DIALOG_STEP1_NO_NAME = 0;
	private static final int DIALOG_STEP1_NO_SERIAL = 1;
	private static final int DIALOG_STEP2_NO_SEED = 2;
	private static final int DIALOG_STEP2_INVALID_SEED = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.token_add);
		
		loadSpinnerArrayData(R.id.tokenTypeSpinner, R.array.tokenType);
		loadSpinnerArrayData(R.id.tokenOtpSpinner, R.array.otpLength);
		loadSpinnerArrayData(R.id.tokenTimeStepSpinner, R.array.timeStep);
		
		Button btnNext = (Button)findViewById(R.id.btnAddStep2);
		btnNext.setOnClickListener(buttonNext);
		
		RadioButton rbManual = (RadioButton)findViewById(R.id.rbSeedManual);
		RadioButton rbRandom = (RadioButton)findViewById(R.id.rbSeedRandom);
		RadioButton rbPassword = (RadioButton)findViewById(R.id.rbSeedPassword);
		
		rbManual.setOnClickListener(radioSeed);
		rbRandom.setOnClickListener(radioSeed);
		rbPassword.setOnClickListener(radioSeed);
		
		Button btnComplete = (Button)findViewById(R.id.tokenAddComplete);
		btnComplete.setOnClickListener(buttonComplete);	
		
		Spinner tokenType = (Spinner)findViewById(R.id.tokenTypeSpinner);
		tokenType.setOnItemSelectedListener(tokenTypeSelected);
	}
	
	
	private OnItemSelectedListener tokenTypeSelected = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			TextView caption = (TextView)findViewById(R.id.tokenTimeStep);
			Spinner spinner = (Spinner)findViewById(R.id.tokenTimeStepSpinner);
			
			if(arg2 == 0){
				caption.setVisibility(View.INVISIBLE);
				spinner.setVisibility(View.INVISIBLE);
			}else{
				caption.setVisibility(View.VISIBLE);
				spinner.setVisibility(View.VISIBLE);
			}
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d;
		
		switch(id){
		case DIALOG_STEP1_NO_NAME:
			d = createAlertDialog(R.string.tokenAddDialogNoName);
			break;
			
		case DIALOG_STEP1_NO_SERIAL:
			d = createAlertDialog(R.string.tokenAddDialogNoSerial);
			break;
			
		case DIALOG_STEP2_NO_SEED:
			d = createAlertDialog(R.string.tokenAddDialogNoSeed);
			break;
			
		case DIALOG_STEP2_INVALID_SEED:
			d = createAlertDialog(R.string.tokenAddDialogInvalidSeed);
			break;
			
		default:
			d = null;
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


	private OnClickListener buttonNext = new OnClickListener() {
		
		public void onClick(View v) {
			//validate we have the required completed fields
			
			boolean isValid = true;
			
			String name = ((EditText)findViewById(R.id.tokenNameEdit)).getText().toString();
			String serial = ((EditText)findViewById(R.id.tokenSerialEdit)).getText().toString();
			
			if(name.length() == 0){
				isValid = false;
				showDialog(DIALOG_STEP1_NO_NAME);
				
			}else if(serial.length() == 0){
				isValid = false;
				showDialog(DIALOG_STEP1_NO_SERIAL);
			}
			
			if(isValid){
				//show the next step
				LinearLayout step1 = (LinearLayout)findViewById(R.id.tokenAddStep1);
				LinearLayout step2 = (LinearLayout)findViewById(R.id.tokenAddStep2);
				
				step1.setVisibility(View.GONE);
				step2.setVisibility(View.VISIBLE);
			}
		}
	};
	
	private OnClickListener buttonComplete = new OnClickListener() {
		
		public void onClick(View v) {
			//validate we have a valid serial
			Boolean isValid = true;
			
			RadioButton rbPassword = (RadioButton)findViewById(R.id.rbSeedPassword);
			String name = ((EditText)findViewById(R.id.tokenNameEdit)).getText().toString();
			String serial = ((EditText)findViewById(R.id.tokenSerialEdit)).getText().toString();
			String seed = ((EditText)findViewById(R.id.tokenSeedEdit)).getText().toString();
			int tokenType = ((Spinner)findViewById(R.id.tokenTypeSpinner)).getSelectedItemPosition();
			int otpLength = Integer.parseInt(((Spinner)findViewById(R.id.tokenOtpSpinner)).getSelectedItem().toString());
			int timeStep;
			
			Spinner tokenTimeStepSpinner = (Spinner)findViewById(R.id.tokenTimeStepSpinner);
					
			switch(tokenTimeStepSpinner.getSelectedItemPosition()){
				case 0:
				default:
					timeStep = 30;
					
				case 1:
					timeStep = 60;
			}
			
			if(seed.length() == 0){
				isValid = false;
				showDialog(DIALOG_STEP2_NO_SEED);
				return;
			}
			
			if(!rbPassword.isChecked()){
			
				//validate the length
				int seedLength = seed.length();
				
				if(seedLength != 32 & seedLength != 40){
					isValid = false;
					showDialog(DIALOG_STEP2_INVALID_SEED);
					return;
				}
				
				//valid the chars in the seed
				Pattern p = Pattern.compile("[A-Fa-f0-9]*");
				Matcher matcher = p.matcher(seed);
				
				if(!matcher.matches()){
					showDialog(DIALOG_STEP2_INVALID_SEED);
					return;
				}				
			}
			
			//TODO password seed
			
			if(isValid){
				//store token in db
				TokenDbAdapter db = new TokenDbAdapter(v.getContext());
				db.open();
				db.createToken(name, serial, seed, tokenType, otpLength, timeStep);
				db.close();
				
				finish();
			}
			
		}
	};
	
	private OnClickListener radioSeed = new OnClickListener() {
		
		public void onClick(View v) {
			RadioButton rb = (RadioButton)v;
			
			if(rb.getId() == R.id.rbSeedRandom){
				EditText seedEdit = (EditText)findViewById(R.id.tokenSeedEdit);
				seedEdit.setText(HotpToken.generateNewSeed(128));
			}
		}
	};
	
	private void loadSpinnerArrayData(int spinnerId, int arrayData){
		Spinner spinner = (Spinner)findViewById(spinnerId);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayData, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

}
