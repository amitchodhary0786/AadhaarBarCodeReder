package com.example.aadhaarbarcodereder.activity.encryption;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
 
public class digitalSignature {	
    @RequiresApi(api = Build.VERSION_CODES.O)
	public static byte[] readFileBytes(String filename) throws IOException
    {
        Path path = Paths.get(filename);
        return Files.readAllBytes(path);        
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
	public static PublicKey readPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);       
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
	public static PrivateKey readPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
    	byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);     
    }  
    
	
	public static String encrypt(PublicKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
	  { 
		  Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
		  cipher.init(Cipher.ENCRYPT_MODE, key);
		  return cipher.doFinal(plaintext).toString(); 	  
      }
	   

    public static String decrypt(PrivateKey key, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");   
        cipher.init(Cipher.DECRYPT_MODE, key);  
        return cipher.doFinal(ciphertext).toString();
    }
    
	/*
	 * public String sign(PrivateKey privateKey, byte[] secret) throws
	 * NoSuchAlgorithmException, InvalidKeyException, SignatureException,
	 * UnsupportedEncodingException { Signature sign =
	 * Signature.getInstance("SHA1withRSA"); sign.initSign(privateKey);
	 * sign.update(secret); return new
	 * String(Base64.getDecoder().decode(sign.sign()), "UTF-8"); }
	 */
    
    @RequiresApi(api = Build.VERSION_CODES.O)
	public static String sign(String data, PrivateKey privateKey) throws InvalidKeyException, Exception{
		
    	 Signature privateSignature = Signature.getInstance("SHA256withRSA");	
		  privateSignature.initSign(privateKey);
		  privateSignature.update(data.getBytes("UTF-8"));
		  byte[] s = privateSignature.sign(); 
		  return Base64.getEncoder().encodeToString(s);		     
	}
    

	@RequiresApi(api = Build.VERSION_CODES.O)
	static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initVerify(publicKey);
		sig.update(data);
		
		return sig.verify(Base64.getDecoder().decode(signature));
	}

    
	
	@RequiresApi(api = Build.VERSION_CODES.O)
	public static boolean verifySign(PublicKey publicKey, String hash, String signature) throws SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		
		  Signature sign = Signature.getInstance("SHA256withRSA");
		  sign.initVerify(publicKey); 
		  sign.update(hash.getBytes("UTF-8"));
		  boolean result = sign.verify(Base64.getDecoder().decode(signature)); 
		  return result;	      	
    }  
       
}