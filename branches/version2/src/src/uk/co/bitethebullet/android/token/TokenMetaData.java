package uk.co.bitethebullet.android.token;

public class TokenMetaData implements ITokenMeta {

	public static final int HOTP_TOKEN = 0;
	public static final int TOTP_TOKEN = 1;
	
	public TokenMetaData(){
		
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTokenType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getSecretBase32() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getDigits() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTimeStep() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCounter() {
		// TODO Auto-generated method stub
		return 0;
	}

}
