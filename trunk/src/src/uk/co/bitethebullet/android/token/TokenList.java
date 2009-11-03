/*
 * Copyright Mark McAvoy - www.bitethebullet.co.uk 2009
 * 
 * This file is part of Android Token.
 *
 * Android Token is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Android Token is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Android Token.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.co.bitethebullet.android.token;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Main entry point into Android Token application
 * 
 * Generates OATH compliant HOTP or TOTP tokens which can be used
 * in place of a hardware token.
 * 
 * For more information about this project visit
 * http://code.google.com/p/androidtoken/
 */
public class TokenList extends ListActivity {
	
	private static final int ACTIVITY_ADD_TOKEN = 0;
	private static final int ACTIVITY_CHANGE_PIN = 1;
	private static final int ACTIVITY_REMOVE_PIN = 2;
	
	private static final int MENU_ADD_ID = Menu.FIRST;
	private static final int MENU_PIN_CHANGE_ID = Menu.FIRST + 1;
	private static final int MENU_PIN_REMOVE_ID = Menu.FIRST + 2;
	
	private static final int DIALOG_INVALID_PIN = 0;
	
	private static final String KEY_HAS_PASSED_PIN = "pinValid";
	
	private Boolean mHasPassedPin = false;
	
	private LinearLayout mMainPin;
	private LinearLayout mMainList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //check if we need to restore from a saveinstancestate
        if(savedInstanceState != null){
        	mHasPassedPin = savedInstanceState.getBoolean(KEY_HAS_PASSED_PIN);
        }
        
        
        //if we have a pin defined, need to enter that first before allow
        //the user to see the tokens        
        mMainPin = (LinearLayout)findViewById(R.id.mainPin);
        mMainList = (LinearLayout)findViewById(R.id.mainList);
        
        Button loginBtn = (Button)findViewById(R.id.mainLogin);
        
        loginBtn.setOnClickListener(validatePin);
        
        if(PinManager.hasPinDefined(this) & !mHasPassedPin){
        	mMainPin.setVisibility(View.VISIBLE);
        	mMainList.setVisibility(View.GONE);
        }else{
        	mMainList.setVisibility(View.VISIBLE);
        	mMainPin.setVisibility(View.GONE);
        	mHasPassedPin = true;
        	fillData();
        }
    }
    

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_HAS_PASSED_PIN, mHasPassedPin);
	}

	private OnClickListener validatePin = new 	OnClickListener() {
		
		public void onClick(View v) {
			
			String pin = ((EditText)findViewById(R.id.mainPinEdit)).getText().toString();
			
			if(PinManager.validatePin(v.getContext(), pin)){
				//then display the list view
				mMainList.setVisibility(View.VISIBLE);
				mMainPin.setVisibility(View.GONE);
				mHasPassedPin = true;
				fillData();
			}else{
				//display an alert
				showDialog(DIALOG_INVALID_PIN);
			}
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


	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d;
		
		switch(id){
		
		case DIALOG_INVALID_PIN:
			d = createAlertDialog(R.string.pinAlertInvalidPin);
			break;
			
		default:
			d = null;
		
		}
		
		return d;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		if(mHasPassedPin){
			menu.getItem(2).setEnabled(PinManager.hasPinDefined(this));
		}
		
		return mHasPassedPin;
	}

	private void fillData() {
		TokenDbAdapter db = new TokenDbAdapter(this);
		db.open();
		
		Cursor c = db.fetchAllTokens();
		startManagingCursor(c);
		
		String[] from = new String[] {TokenDbAdapter.KEY_TOKEN_NAME, TokenDbAdapter.KEY_TOKEN_SERIAL};
		int[] to = new int[] {R.id.tokenrowtextname, R.id.tokenrowtextserial};
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.token_list_row, c, from, to);
		setListAdapter(adapter);		
		
		db.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		menu.add(0, MENU_ADD_ID, 0, R.string.menu_add).setIcon(android.R.drawable.ic_menu_add);	
		menu.add(0, MENU_PIN_CHANGE_ID, 1, R.string.menu_pin_change).setIcon(android.R.drawable.ic_lock_lock);
		menu.add(0, MENU_PIN_REMOVE_ID, 2, R.string.menu_pin_remove).setIcon(android.R.drawable.ic_menu_delete);
		return true;
	}

	

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case MENU_ADD_ID:
			createToken();
			return true;
			
		case MENU_PIN_CHANGE_ID:
			changePin();
			return true;
			
		case MENU_PIN_REMOVE_ID:
			removePin();
			return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	private void removePin() {
		Intent i = new Intent(this, PinRemove.class);
		startActivityForResult(i, ACTIVITY_REMOVE_PIN);
	}

	private void changePin() {
		Intent i = new Intent(this,	 PinChange.class);
		startActivityForResult(i, ACTIVITY_CHANGE_PIN);
	}

	/**
	 * Starts the process of creating a new token in the application
	 */
	private void createToken() {
		Intent intent = new Intent(this, TokenAdd.class);
		startActivityForResult(intent, ACTIVITY_ADD_TOKEN);		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		TokenDbAdapter db = new TokenDbAdapter(this);
		db.open();
		
		Cursor cursor = db.fetchToken(id);
		IToken token = TokenFactory.CreateToken(cursor);
		cursor.close();
		
		String otp = token.GenerateOtp();		
		db.incrementTokenCount(id);
		db.close();
		
		Toast.makeText(this, otp, Toast.LENGTH_LONG).show();		
		
	}
}