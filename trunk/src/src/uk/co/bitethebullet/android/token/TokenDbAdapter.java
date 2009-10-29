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

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Performs the CRUD database actions for the Android Token application
 */
public class TokenDbAdapter {
	//const holding the table field names
	public static final String KEY_TOKEN_ROWID = "_id";
	public static final String KEY_TOKEN_NAME = "name";
	public static final String KEY_TOKEN_SERIAL = "serial";
	public static final String KEY_TOKEN_SEED = "seed";
	public static final String KEY_TOKEN_COUNT = "eventcount";
	public static final String KEY_TOKEN_TYPE = "tokentype";
	
	public static final String KEY_PIN_ROWID = "_id";
	public static final String KEY_PIN_HASH = "pinHash";
	
	//const define the different token type
	public static final int TOKEN_TYPE_EVENT = 1;
	public static final int TOKEN_TYPE_TIME = 2;
	
	public static final String TAG = "TokenDbAdapter";
	
	//const database tables, version
	private static final String DATABASE_NAME = "androidtoken";
    private static final String DATABASE_TOKEN_TABLE = "token";
    private static final String DATABASE_PIN_TABLE = "pin";
    private static final int DATABASE_VERSION = 1;
    
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mContext;
	
    
    private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context){
			super(context,DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}    	
    }
    
    public TokenDbAdapter(Context context){
    	this.mContext = context;
    }
    
    public TokenDbAdapter open() throws SQLException{
    	mDbHelper = new DatabaseHelper(mContext);
    	mDb = mDbHelper.getWritableDatabase();
    	return this;
    }
    
    public void close(){
    	mDbHelper.close();
    }
}
