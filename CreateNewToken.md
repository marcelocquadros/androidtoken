# Introduction #

Android Token supports the ability to define multiple tokens. A single instance of Android Token can replace multiple hardware tokens that you would otherwise need to carry.

You can create tokens which are Event Tokens ([HOTP](http://tools.ietf.org/html/rfc4226)) or Time Tokens ([TOTP](http://tools.ietf.org/html/draft-mraihi-totp-timebased-00)) with complete control other the settings for each token.

# Details #

Creating a new token is a two stage process. Firstly you define the basic settings for the token then you entry or generate the seed for the token.

**The seed value is used to generate the OTP value. You should protect the seed value and keep this value private.**

## Step 1 ##

http://androidtoken.googlecode.com/svn/trunk/docs/images/createStep1.Png

  1. Select the type of token to create, this can either be an Event Token or a Time Token
  1. Enter a name for the token, this is the name which will be displayed for the token.
  1. Enter a serial number, your server administrator should be able to supply you this value.
  1. Select the length of the OTP that the application will generate
  1. For Time Tokens you should also set the time step of either 30 seconds or 60 seconds. Your server administrator will be able to supply the correct value.

## Step 2 ##

http://androidtoken.googlecode.com/svn/trunk/docs/images/createStep2.Png

You now need to enter the seed value for the token. Here you have three options to entry/generate the seed.

  1. **Direct Entry** - Select this option to enter the hexadecimal value used as the seed. This should be either 128 or 160 bit in length
  1. **Generate Random Seed** - Select this option to have the application generate a random 160 bit seed value. You will need to write this down and tell you're server administrator this value.
  1. **Seed from Password** - This will generate a 160 bit seed using a alphanumeric password. Getting a user to enter a hexadecimal string into the Direct Entry option could be come cumbersome, therefore this is a simple way of generating a seed which is easy to enter for the user. (You can read more about Seed from Password below)

# Server Administrator Guide #

Android Token is a OATH software token for the Android platform, tokens defined in application can be used as direct replacement for hardware tokens.

The application supports all the options defined in both specification.

As a minimum you should provider each Android Token user with the following information to setup the token

  1. **Token Type** - Defines the token as either Event or Time based
  1. **Serial** - This should a unique alpha-numeric value which defines the token within your OATH server.
  1. **OTP Length** - Defines the length of the OTP produced
  1. **Time Step** - (Only required for Time Tokens) Defines the time in seconds that need to elapse before a new OTP is generated.
  1. **Seed Value** - This should either be a hexadecimal string of 128 or 160 bits or a password used to create a seed using _Seed from Password_ function.

**Its important that all the information above matches the information held on the OATH servre for that token.**

## Seed from Password ##

This is provided as an easy and secure way for the user to generate a seed without having to enter a complex hexadecimal value.

The password is hashed using SHA1 then concatenate with itself before being hashed a second time to generate a seed value of 160 bits.

_h1 = sha1(password)_

_h2 = sha1(password + h1)_


You should simple apply the same logic to generate the seed for the token which can then stored in your OATH server.