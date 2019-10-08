package com.kingh.utils;

public class HexUtils {
	private static final char[] hex = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	public HexUtils() {
	}

	public static final byte[] fromHex(String b) {
		char[] data = b.toCharArray();
		int l = data.length;
		byte[] out = new byte[l >> 1];
		int i = 0;

		for (int j = 0; j < l; ++i) {
			int f = Character.digit(data[j++], 16) << 4;
			f |= Character.digit(data[j++], 16);
			out[i] = (byte) (f & 255);
		}

		return out;
	}

	public static final String toHex(byte[] b) {
		char[] buf = new char[b.length * 2];
		int j = 0;

		for (int i = 0; i < b.length; ++i) {
			byte k = b[i];
			buf[j++] = hex[k >>> 4 & 15];
			buf[j++] = hex[k & 15];
		}

		return new String(buf);
	}

	public static void main(String[] args) {
		String s = "E5BCA0E58D95E58D95";
		try {
			s = new String(HexUtils.fromHex(s));
		} catch (Exception e) {
		}
		System.out.println(s);
	}
}
