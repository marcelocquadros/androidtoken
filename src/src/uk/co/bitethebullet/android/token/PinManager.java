package uk.co.bitethebullet.android.token;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.database.Cursor;

public class PinManager {

	private static final String SALT = "EE08F4A6-8497-4330-8CD5-8A4ABD93CD46";
	
	public static Boolean hasPinDefined(Context c){
		TokenDbAdapter db = new TokenDbAdapter(c);
		db.open();
		
		Boolean hasPin = db.fetchPin() != null;
		db.close();
		
		return hasPin;
	}
	
	public static Boolean validatePin(Context c, String pin){

		TokenDbAdapter db = new TokenDbAdapter(c);
		db.open();

		Boolean isValid = false;
		String userPin = createPinHash(pin);
		Cursor cursor = db.fetchAllTokens();
		
		if(cursor != null){
			String dbPin = cursor.getString(cursor.getColumnIndexOrThrow(TokenDbAdapter.KEY_PIN_HASH));
			isValid = dbPin.contentEquals(userPin);
		}
		
		cursor.close();		
		db.close();
		
		return isValid;
	}
	
	public static void storePin(Context c, String pin){
		//TODO complete me
	}
	
	private static String createPinHash(String pin) {
		
		try{
			
			String toHash = SALT + pin;
			
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.reset();
			md.update(toHash.getBytes());
			byte[] hashOutput = md.digest();
			
			return HotpToken.byteArrayToHexString(hashOutput);			
			
		}catch(NoSuchAlgorithmException ex){
			return null;
		}
		
	}
	
}
