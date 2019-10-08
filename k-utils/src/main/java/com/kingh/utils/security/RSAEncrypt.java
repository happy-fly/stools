package com.kingh.utils.security;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.ArrayUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class RSAEncrypt {  
    /** 
     * 字节数据转字符串专用集合 
     */  
    private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6',  
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
  
    /** 
     * 随机生成密钥对 
     */  
    public static void genKeyPair(String filePath) {  
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
        KeyPairGenerator keyPairGen = null;  
        try {  
            keyPairGen = KeyPairGenerator.getInstance("RSA");  
        } catch (NoSuchAlgorithmException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        // 初始化密钥对生成器，密钥大小为96-1024位  
        keyPairGen.initialize(1024,new SecureRandom());  
        // 生成一个密钥对，保存在keyPair中  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        // 得到私钥  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        // 得到公钥  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        try {  
            // 得到公钥字符串  
            String publicKeyString = Base64.encode(publicKey.getEncoded());
            // 得到私钥字符串  
            String privateKeyString = Base64.encode(privateKey.getEncoded());
            
            
            System.out.println("公钥串：" + publicKeyString);
            
            System.out.println("私钥串：" + privateKeyString);
            // 将密钥对写入到文件  
            FileWriter pubfw = new FileWriter(filePath + "/publicKey.keystore");  
            FileWriter prifw = new FileWriter(filePath + "/privateKey.keystore");  
            BufferedWriter pubbw = new BufferedWriter(pubfw);  
            BufferedWriter pribw = new BufferedWriter(prifw);  
            pubbw.write(publicKeyString);  
            pribw.write(privateKeyString);  
            pubbw.flush();  
            pubbw.close();  
            pubfw.close();  
            pribw.flush();  
            pribw.close();  
            prifw.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 从文件中输入流中加载公钥 
     *  
     * @param path
     *            公钥输入流 
     * @throws Exception 
     *             加载公钥时产生的异常 
     */  
    public static String loadPublicKeyByFile(String path) throws Exception {
        try {  
            BufferedReader br = new BufferedReader(new FileReader(path  
                    + "/publicKey.keystore"));  
            String readLine = null;  
            StringBuilder sb = new StringBuilder();  
            while ((readLine = br.readLine()) != null) {  
                sb.append(readLine);  
            }  
            br.close();  
            return sb.toString();  
        } catch (IOException e) {  
            throw new Exception("公钥数据流读取错误");  
        } catch (NullPointerException e) {  
            throw new Exception("公钥输入流为空");  
        }  
    }  
  
    /** 
     * 从字符串中加载公钥 
     *  
     * @param publicKeyStr 
     *            公钥数据字符串 
     * @throws Exception 
     *             加载公钥时产生的异常 
     */  
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)  
            throws Exception {  
        try {  
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);  
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此算法");  
        } catch (InvalidKeySpecException e) {  
            throw new Exception("公钥非法");  
        } catch (NullPointerException e) {  
            throw new Exception("公钥数据为空");  
        }  
    }  
  
    /** 
     * 从文件中加载私钥 
     *  
     * @param path
     *            私钥文件名 
     * @return 是否成功 
     * @throws Exception 
     */  
    public static String loadPrivateKeyByFile(String path) throws Exception {  
        try {  
            BufferedReader br = new BufferedReader(new FileReader(path  
                    + "/privateKey.keystore"));  
            String readLine = null;  
            StringBuilder sb = new StringBuilder();  
            while ((readLine = br.readLine()) != null) {  
                sb.append(readLine);  
            }  
            br.close();  
            return sb.toString();  
        } catch (IOException e) {  
            throw new Exception("私钥数据读取错误");  
        } catch (NullPointerException e) {  
            throw new Exception("私钥输入流为空");  
        }  
    }  
  
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)  
            throws Exception {  
        try {  
            byte[] buffer = Base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此算法");  
        } catch (InvalidKeySpecException e) {  
            throw new Exception("私钥非法");  
        } catch (NullPointerException e) {  
            throw new Exception("私钥数据为空");  
        }  
    }  
  
    /** 
     * 公钥加密过程 
     *  
     * @param publicKey 
     *            公钥 
     * @param plainTextData 
     *            明文数据 
     * @return 
     * @throws Exception 
     *             加密过程中的异常信息 
     */  
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)  
            throws Exception {  
        if (publicKey == null) {  
            throw new Exception("加密公钥为空, 请设置");  
        }  
        Cipher cipher = null;  
        try {  
            // 使用默认RSA  
            cipher = Cipher.getInstance("RSA");  
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());  
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
            byte[] output=null;
            
            StringBuilder sb = new StringBuilder();
			for (int i = 0; i < plainTextData.length; i += 100) {
				byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(plainTextData, i,
						i + 100));
				sb.append(new String(doFinal));
				output = ArrayUtils.addAll(output, doFinal);
			}
            
            
            
//            byte[] output = cipher.doFinal(plainTextData);  
            return output;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此加密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        } catch (InvalidKeyException e) {  
            throw new Exception("加密公钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("明文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("明文数据已损坏");  
        }  
    }  
  
    /** 
     * 私钥加密过程 
     *  
     * @param privateKey 
     *            私钥 
     * @param plainTextData 
     *            明文数据 
     * @return 
     * @throws Exception 
     *             加密过程中的异常信息 
     */  
    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData)  
            throws Exception {  
        if (privateKey == null) {  
            throw new Exception("加密私钥为空, 请设置");  
        }  
        Cipher cipher = null;  
        try {  
            // 使用默认RSA  
            cipher = Cipher.getInstance("RSA");  
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
            byte[] output = cipher.doFinal(plainTextData);  
            return output;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此加密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        } catch (InvalidKeyException e) {  
            throw new Exception("加密私钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("明文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("明文数据已损坏");  
        }  
    }  
  
    /** 
     * 私钥解密过程 
     *  
     * @param privateKey 
     *            私钥 
     * @param cipherData 
     *            密文数据 
     * @return 明文 
     * @throws Exception 
     *             解密过程中的异常信息 
     */  
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)  
            throws Exception {  
        if (privateKey == null) {  
            throw new Exception("解密私钥为空, 请设置");  
        }  
        Cipher cipher = null;  
        try {  
            // 使用默认RSA  
            cipher = Cipher.getInstance("RSA");  
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());  
            cipher.init(Cipher.DECRYPT_MODE, privateKey);  
//            byte[] output = cipher.doFinal(cipherData);  
            
            byte[] output;
            
            StringBuilder sb = new StringBuilder();
			for (int i = 0; i < cipherData.length; i += 128) {
				byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(cipherData, i,
						i + 128));
				sb.append(new String(doFinal,"UTF-8"));
			}
			output = sb.toString().getBytes("UTF-8");
            
            
            return output;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此解密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        } catch (InvalidKeyException e) {  
            throw new Exception("解密私钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("密文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("密文数据已损坏");  
        }  
    }  
  
    
    
    
    
    /** 
     * 私钥解密过程 -New
     *  
     * @param privateKey 
     *            私钥 
     * @param cipherData 
     *            密文数据 
     * @return 明文 
     * @throws Exception 
     *             解密过程中的异常信息 
     */  
    public static byte[] decryptNew(RSAPrivateKey privateKey, byte[] cipherData)  
            throws Exception {  
        if (privateKey == null) {  
            throw new Exception("解密私钥为空, 请设置");  
        }  
        Cipher cipher = null;  
        try {  
            // 使用默认RSA  
            cipher = Cipher.getInstance("RSA");  
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());  
            cipher.init(Cipher.DECRYPT_MODE, privateKey);  
//            byte[] output = cipher.doFinal(cipherData);  
            
            List<Byte> list =new ArrayList<Byte>();
            
			for (int i = 0; i < cipherData.length; i += 128) {
				byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(cipherData, i,
						i + 128));
				
				for(byte b : doFinal){
					list.add(b);
				}
			}
			byte[] array = new byte[list.size()];
            for(int i=0; i<list.size(); i++){
            	array[i] = list.get(i);
            }
            
            return array;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此解密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        } catch (InvalidKeyException e) {  
            throw new Exception("解密私钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("密文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("密文数据已损坏");  
        }  
    }  
    
    
    /** 
     * 公钥解密过程 
     *  
     * @param publicKey 
     *            公钥 
     * @param cipherData 
     *            密文数据 
     * @return 明文 
     * @throws Exception 
     *             解密过程中的异常信息 
     */  
    public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData)  
            throws Exception {  
        if (publicKey == null) {  
            throw new Exception("解密公钥为空, 请设置");  
        }  
        Cipher cipher = null;  
        try {  
            // 使用默认RSA  
            cipher = Cipher.getInstance("RSA");  
            // cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());  
            cipher.init(Cipher.DECRYPT_MODE, publicKey);  
            byte[] output = cipher.doFinal(cipherData);  
            return output;  
        } catch (NoSuchAlgorithmException e) {  
            throw new Exception("无此解密算法");  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
            return null;  
        } catch (InvalidKeyException e) {  
            throw new Exception("解密公钥非法,请检查");  
        } catch (IllegalBlockSizeException e) {  
            throw new Exception("密文长度非法");  
        } catch (BadPaddingException e) {  
            throw new Exception("密文数据已损坏");  
        }  
    }  
  
    /** 
     * 字节数据转十六进制字符串 
     *  
     * @param data 
     *            输入数据 
     * @return 十六进制内容 
     */  
    public static String byteArrayToString(byte[] data) {  
        StringBuilder stringBuilder = new StringBuilder();  
        for (int i = 0; i < data.length; i++) {  
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移  
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);  
            // 取出字节的低四位 作为索引得到相应的十六进制标识符  
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);  
            if (i < data.length - 1) {  
                stringBuilder.append(' ');  
            }  
        }  
        return stringBuilder.toString();  
    }
    
    
	public static HashMap<String, String> toHashMap(String jsonStr) {
		HashMap<String, String> data = new HashMap<String, String>();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = JSONObject.parseObject(jsonStr);
		Iterator<?> it = jsonObject.keySet().iterator();
		// 遍历jsonObject数据，添加到Map对象
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			String value = (String) jsonObject.get(key);
			data.put(key, value);
		}
		return data;
	}
    
	
	
	
	 /** 
     * 随机生成密钥对 
     */  
    public static Map<String, String> genKeyPairMap() {  
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
    	Map<String, String> keyPairMap = new HashMap<String, String>();
        KeyPairGenerator keyPairGen = null;  
        try {  
            keyPairGen = KeyPairGenerator.getInstance("RSA");  
        } catch (NoSuchAlgorithmException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        // 初始化密钥对生成器，密钥大小为96-1024位  
        keyPairGen.initialize(1024,new SecureRandom());  
        // 生成一个密钥对，保存在keyPair中  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        // 得到私钥  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        // 得到公钥  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        try {  
            // 得到公钥字符串  
            String publicKeyString = Base64.encode(publicKey.getEncoded());
            // 得到私钥字符串  
            String privateKeyString = Base64.encode(privateKey.getEncoded());
            
            
            System.out.println("公钥串：" + publicKeyString);
            
            System.out.println("私钥串：" + privateKeyString);
            
            keyPairMap.put("publicKey", publicKeyString);
            keyPairMap.put("privateKey", privateKeyString);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return keyPairMap;
    }  
    
    
    public static void main(String[] args) throws Exception{
//    	String filePath = "E:/keystore";
    	String content = "0922";
    	System.out.println("原内容为：" + content);
    	RSAPublicKey publicKey = loadPublicKeyByStr("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLkjcvvF2q/yEVzpZZ+1jOUX9U12lclNfPV178slPp1O+0BauR2p50Q0qCUH82iqXn8rwrj7C13UwJ0HAOx4Cn3h1bJRPwJmulmYaZAF7OwfQYDnOlrxXJyfk8kXKQza0B92cPBsa7Ko4X5WOqnCN/dM37Wy1mX4G14RgwnW/E8wIDAQAB");
    	RSAPrivateKey privateKey = loadPrivateKeyByStr("MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMuSNy+8Xar/IRXOlln7WM5Rf1TXaVyU189XXvyyU+nU77QFq5HannRDSoJQfzaKpefyvCuPsLXdTAnQcA7HgKfeHVslE/Ama6WZhpkAXs7B9BgOc6WvFcnJ+TyRcpDNrQH3Zw8GxrsqjhflY6qcI390zftbLWZfgbXhGDCdb8TzAgMBAAECgYEAqvi4hM0NVkpEfU4ulJA2+8ES0izA/tK7lhZpXo5fT6pZT7bba/pSzo6di2kUDrjPskj5pRRfc77S2ANJuf/wmovp6kZo6uPIq+ksidLfHU1U6seHAh7gA79H1K+28Xv4fHYQIHoFvv/n/3PLhBYSukqdKDN69PrwF6ssZ27wwpECQQD0OVKzXRR73wT7Z+ZweCaQ3TtBdY+31LaCXZKF7ntyH022yYHTQue8oKO+j5JfIpxwcTcNWrm/tp440iaNiSH9AkEA1WMUIqHujicDkEkoDIiyYnj4VSMsqclf22Gjetpo23l/iRWg384poe4p5RY7fEWUjGzT2pzJJ2SJAWVFxk19rwJAfnSH+9OAXbtWiw57ZVTxMyU6H2TN67O6ZLoquiqHxMmspa6lCEymTScoYe1ZLuVu91HH3aRSdZALUQSPbc3JyQJBALs8KbHNxsWixAS5vFNrKqMvJC+IIquEUADuZoTo2jMiI98ERFm8y1kT4lzqKh3768FfyHC9O9+93l2EIDpFSm8CQQDObZQlTS/JMU643WxOmr8p+auwmrps+Y5fYyCYiHh62+kg4kMc3En9mKxMqTcAHhzCHXTGZuKkhVekpdtLJwtT");
    	
    	byte[] encryptStrByPub = encrypt(publicKey, content.getBytes("UTF-8"));
//    	String encryptResult = Base64.encode(encryptStrByPub);
//    	String encryptResult = new String(encryptStrByPub,"UTF-8");
    	String encryptResult = new String(java.util.Base64.getEncoder().encode(encryptStrByPub));
    	System.out.println("加密串为：" + encryptResult);
    	
    	
    	byte[] decryptStr = decrypt(privateKey, Base64.decode(encryptResult));
    	String decryptResult = new String(decryptStr, "UTF-8");
    	System.out.println("解密结果为：" + decryptResult);
    	
    }
}  