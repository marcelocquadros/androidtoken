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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

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
	
	private static final int MENU_ADD_ID = Menu.FIRST;
	private static final int MENU_PIN_CHANGE_ID = Menu.FIRST + 1;
	private static final int MENU_PIN_REMOVE_ID = Menu.FIRST + 2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		menu.add(0, MENU_ADD_ID, 0, R.string.menu_add);	
		menu.add(0, MENU_PIN_CHANGE_ID, 1, R.string.menu_pin_change);
		menu.add(0, MENU_PIN_REMOVE_ID, 2, R.string.menu_pin_remove);
		return true;
	}

	

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case MENU_ADD_ID:
			createToken();
			return true;
			
		case MENU_PIN_CHANGE_ID:
			//TODO: MM to complete
			return true;
			
		case MENU_PIN_REMOVE_ID:
			//TODO: MM to complete
			return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
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
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}
}