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

import java.lang.reflect.UndeclaredThrowableException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * Hotp Token
 * 
 * This is an event based OATH token, for further details
 * see the RFC http://tools.ietf.org/html/rfc4226
 *
 */
public class HotpToken implements IToken {

	private String mName;
	private String mSerial;
	private String mSeed;
	private long mEventCount;
	
	//TODO: MM need to make this parameter and pick it up from the db
	private static final int OTP_LENGTH = 6;
	
	
	private static final int[] DIGITS_POWER
    // 0 1  2   3    4     5      6       7        8
    = {1,10,100,1000,10000,100000,1000000,10000000,100000000};

	
	public HotpToken(String name, String serial, String seed, long eventCount){
		mName = name;
		mSerial = serial;
		mSeed = seed;
		mEventCount = eventCount;
	}
	
	
	public String getName() {
		return mName;
	}


	public void setName(String name) {
		this.mName = name;
	}


	public String getSerial() {
		return mSerial;
	}


	public void setmSerial(String serial) {
		this.mSerial = serial;
	}


	protected String getSeed() {
		return mSeed;
	}


	protected void setmSeed(String seed) {
		this.mSeed = seed;
	}


	protected long getEventCount() {
		return mEventCount;
	}


	protected void setmEventCount(long eventCount) {
		this.mEventCount = eventCount;
	}


	public String GenerateOtp() {
		
		byte[] counter = new byte[8];
		long movingFactor = mEventCount;
		
		for(int i = counter.length - 1; i >= 0; i--){
			counter[i] = (byte)(movingFactor & 0xff);
			movingFactor >>= 8;
		}
		
		byte[] hash = hmacSha(stringToHex(mSeed), counter);
		int offset = hash[hash.length - 1] & 0xf;
		
		int otpBinary = ((hash[offset] & 0x7f) << 24)
						|((hash[offset + 1] & 0xff) << 16)
						|((hash[offset + 2] & 0xff) << 8)
						|(hash[offset + 3] & 0xff);
		
		int otp = otpBinary % DIGITS_POWER[OTP_LENGTH];
		String result = Integer.toString(otp);
		
		
		while(result.length() < OTP_LENGTH){
			result = "0" + result;
		}
		
		return result;		
	}

	private byte[] stringToHex(String hexInputString){
		
		byte[] bts = new byte[hexInputString.length() / 2];
		
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hexInputString.substring(2*i, 2*i+2), 16);
		}
		
		return bts;
	}

	private byte[] hmacSha(byte[] seed, byte[] counter) {
		
		try{
			Mac hmacSha1;
			
			try{
				hmacSha1 = Mac.getInstance("HmacSHA1");
			}catch(NoSuchAlgorithmException ex){
				hmacSha1 = Mac.getInstance("HMAC-SHA-1");
			}
			
			SecretKeySpec macKey = new SecretKeySpec(seed, "RAW");
			hmacSha1.init(macKey);
			
			return hmacSha1.doFinal(counter);
			
		}catch(GeneralSecurityException ex){
			throw new UndeclaredThrowableException(ex);
		}
	}

}
