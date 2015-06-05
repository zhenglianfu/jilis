package fe.toolkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenericMD5 {

	private final static char HEXDIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static String hexFormat(byte[] seqs) {
		int len = seqs.length;
		char[] chars = new char[len * 2];
		for (int i = 0; i < len; i++) {
			chars[i * 2] = HEXDIGITS[seqs[i] >>> 4 & 0xf];
			chars[i * 2 + 1] = HEXDIGITS[seqs[i] & 0xf];
		}
		return String.valueOf(chars).toLowerCase();
	}

	private static String md5(String squence) throws NoSuchAlgorithmException {
		if (null == squence || "".equals(squence)) {
			return "";
		}
		byte[] bytes = squence.getBytes();
		MessageDigest mdInst = MessageDigest.getInstance("MD5");
		mdInst.update(bytes);
		return hexFormat(mdInst.digest());
	}

	public static String md5(File file) throws IOException,
			NoSuchAlgorithmException {
		if (null != file && file.exists() && file.isFile()) {
			StringBuilder sb = new StringBuilder();
			BufferedReader buffReader = new BufferedReader(new FileReader(file));
			String line = buffReader.readLine();
			while (null != line) {
				sb.append(line);
				line = buffReader.readLine();
			}
			buffReader.close();
			return md5(sb.toString());
		}
		return md5(String.valueOf(System.currentTimeMillis()));
	}
}
