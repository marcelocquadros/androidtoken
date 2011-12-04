package uk.co.bitethebullet.android.token.test;

import uk.co.bitethebullet.android.token.ITokenMeta;
import uk.co.bitethebullet.android.token.OtpAuthUriException;
import uk.co.bitethebullet.android.token.TokenList;
import uk.co.bitethebullet.android.token.TokenMetaData;
import junit.framework.Assert;
import junit.framework.TestCase;

public class ParseUrlTests extends TestCase {

	public void testHotp1() throws OtpAuthUriException{
		String url = "otpauth://hotp/alice@google.com?secret=JBSWY3DPEHPK3PXP&counter=10";
		
		ITokenMeta token = TokenList.parseOtpAuthUrl(url);
		
		Assert.assertEquals("alice@google.com", token.getName());
		Assert.assertEquals("JBSWY3DPEHPK3PXP", token.getSecretBase32());
		Assert.assertEquals(10, token.getCounter());
		Assert.assertEquals(6, token.getDigits());
		Assert.assertEquals(TokenMetaData.HOTP_TOKEN, token.getTokenType());
	}
	
	public void testHotpMissingCounter(){
		String url = "otpauth://hotp/alice@google.com?secret=JBSWY3DPEHPK3PXP";
		
		try {
			ITokenMeta token = TokenList.parseOtpAuthUrl(url);
			Assert.fail();
		} catch (OtpAuthUriException e) {
			e.printStackTrace();
		}		
	}
	
	public void testTotp() throws OtpAuthUriException{
		String url = "otpauth://totp/mark?secret=JBSWY3DPEHPK3PXP";
		
		ITokenMeta token = TokenList.parseOtpAuthUrl(url);
		
		Assert.assertEquals("mark", token.getName());
		Assert.assertEquals("JBSWY3DPEHPK3PXP", token.getSecretBase32());
		Assert.assertEquals(6, token.getCounter());
		Assert.assertEquals(30, token.getTimeStep());
		Assert.assertEquals(TokenMetaData.TOTP_TOKEN, token.getTokenType());
	}
	
	public void testTotpPeriod() throws OtpAuthUriException{
		String url = "otpauth://totp/mark?secret=JBSWY3DPEHPK3PXP&period=60";
		
		ITokenMeta token = TokenList.parseOtpAuthUrl(url);
		
		Assert.assertEquals("mark", token.getName());
		Assert.assertEquals("JBSWY3DPEHPK3PXP", token.getSecretBase32());
		Assert.assertEquals(6, token.getCounter());
		Assert.assertEquals(60, token.getTimeStep());
		Assert.assertEquals(TokenMetaData.TOTP_TOKEN, token.getTokenType());
	}
	
	public void testTotpDigits() throws OtpAuthUriException{
		String url = "otpauth://totp/mark?secret=JBSWY3DPEHPK3PXP&digits=8";
		
		ITokenMeta token = TokenList.parseOtpAuthUrl(url);
		
		Assert.assertEquals("mark", token.getName());
		Assert.assertEquals("JBSWY3DPEHPK3PXP", token.getSecretBase32());
		Assert.assertEquals(8, token.getCounter());
		Assert.assertEquals(30, token.getTimeStep());
		Assert.assertEquals(TokenMetaData.TOTP_TOKEN, token.getTokenType());
	}
	
}
