package util;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import net.rim.device.api.crypto.CryptoTokenException;
import net.rim.device.api.crypto.CryptoUnsupportedOperationException;
import net.rim.device.api.crypto.HMAC;
import net.rim.device.api.crypto.HMACKey;
import net.rim.device.api.crypto.SHA1Digest;
import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.PNGEncodedImage;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;

public class Util {
	public static EncodedImage resizeImage(EncodedImage image, int width,
			int height) {
		EncodedImage result = null;

		int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
		int currentHeightFixed32 = Fixed32.toFP(image.getHeight());

		int requiredWidthFixed32 = Fixed32.toFP(width);
		int requiredHeightFixed32 = Fixed32.toFP(height);

		int scaleXFixed32 = Fixed32.div(currentWidthFixed32,
				requiredWidthFixed32);
		int scaleYFixed32 = Fixed32.div(currentHeightFixed32,
				requiredHeightFixed32);

		result = image.scaleImage32(scaleXFixed32, scaleYFixed32);
		return result;
	}

	public static Bitmap resizeBitmap(Bitmap bmp, int width, int height) {
		PNGEncodedImage encoder = PNGEncodedImage.encode(bmp);
		EncodedImage result = resizeImage(encoder, width, height);
		return result.getBitmap();
	}

	public static String[] split(String strString, String strDelimiter) {
		String[] strArray;
		int iOccurrences = 0;
		int iIndexOfInnerString = 0;
		int iIndexOfDelimiter = 0;
		int iCounter = 0;

		// Check for null input strings.
		if (strString == null) {
			throw new IllegalArgumentException("Input string cannot be null.");
		}
		// Check for null or empty delimiter strings.
		if (strDelimiter.length() <= 0 || strDelimiter == null) {
			throw new IllegalArgumentException(
					"Delimeter cannot be null or empty.");
		}

		// strString must be in this format: (without {} )
		// "{str[0]}{delimiter}str[1]}{delimiter} ...
		// {str[n-1]}{delimiter}{str[n]}{delimiter}"

		// If strString begins with delimiter then remove it in order
		// to comply with the desired format.

		if (strString.startsWith(strDelimiter)) {
			strString = strString.substring(strDelimiter.length());
		}

		// If strString does not end with the delimiter then add it
		// to the string in order to comply with the desired format.
		if (!strString.endsWith(strDelimiter)) {
			strString += strDelimiter;
		}

		// Count occurrences of the delimiter in the string.
		// Occurrences should be the same amount of inner strings.
		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
				iIndexOfInnerString)) != -1) {
			iOccurrences += 1;
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
		}

		// Declare the array with the correct size.
		strArray = new String[iOccurrences];

		// Reset the indices.
		iIndexOfInnerString = 0;
		iIndexOfDelimiter = 0;

		// Walk across the string again and this time add the
		// strings to the array.
		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
				iIndexOfInnerString)) != -1) {

			// Add string to array.
			strArray[iCounter] = strString.substring(iIndexOfInnerString,
					iIndexOfDelimiter);

			// Increment the index to the next character after
			// the next delimiter.
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();

			// Inc the counter.
			iCounter += 1;
		}

		return strArray;
	}

	public static String getTimeStamp() {
		Date d = new Date();
		return Long.toString(d.getTime() / 1000);
	}

	public static final String str = "abcdefghijklmnopqrstuvwxyzABDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final Random rd = new Random();

	public static String getRandomString() {
		String result = "";
		int length = 10;
		for (int i = 0; i < length; i++) {
			int index = rd.nextInt(str.length());
			result += str.substring(index, index + 1);
		}
		return result;
	}

	public static Font getFont(int fontSize) {
		return getFont(fontSize, Font.PLAIN);
	}

	public static Font getFont(int fontSize, int style) {
		try {
			FontFamily family = FontFamily.forName("BBAlpha Sans");
			return family.getFont(style, fontSize, Ui.UNITS_pt);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static String hmacsha1(String key, String message)
			throws CryptoTokenException, CryptoUnsupportedOperationException,
			IOException {
		HMACKey k = new HMACKey(key.getBytes());
		HMAC hmac = new HMAC(k, new SHA1Digest());
		hmac.update(message.getBytes());
		byte[] mac = hmac.getMAC();
		return Base64OutputStream.encodeAsString(mac, 0, mac.length, false,
				false);
	}
}
