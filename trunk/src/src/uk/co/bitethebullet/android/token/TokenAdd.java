package uk.co.bitethebullet.android.token;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TokenAdd extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.token_add);
		
		loadSpinnerArrayData(R.id.tokenTypeSpinner, R.array.tokenType);
		loadSpinnerArrayData(R.id.tokenOtpSpinner, R.array.otpLength);
		loadSpinnerArrayData(R.id.tokenTimeStepSpinner, R.array.timeStep);
	}
	
	
	private void loadSpinnerArrayData(int spinnerId, int arrayData){
		Spinner spinner = (Spinner)findViewById(spinnerId);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayData, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

}
