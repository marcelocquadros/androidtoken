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


/**
 * TOTP Token
 * 
 * Generates an OTP based on the time, for more information see
 * http://tools.ietf.org/html/draft-mraihi-totp-timebased-00
 * 
 */
public class TotpToken extends HotpToken {

	public TotpToken(String name, String serial, String seed){
		super(name, serial, seed, 0);
	}

	@Override
	public String GenerateOtp() {
		
		//todo: MM calculate the movingcounter using the time
		
		return super.GenerateOtp();
	}
	
	
}
