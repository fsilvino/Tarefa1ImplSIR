package tarefa1implsir;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * Esta é a solução da questão 11
 * ----------------------------------------------------------------------------
 * 11. Crie um programa que recebe uma string pelo teclado e cifra a string
 * usando CRIPTOGRAFIA AUTENTICADA. O programa também deve gerar uma boa chave
 * usando PBKDF2.
 * ----------------------------------------------------------------------------
 * @author Flávio e Diogo
 */
public class Questao11 {
    
    private static final int MAC_SIZE = 128;

    public Questao11() {
        System.out.println("--------- Questão 11 ----------");
        try {
            resolver();
        } catch (NoSuchAlgorithmException | DecoderException ex) {
            Logger.getLogger(Questao11.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resolver() throws NoSuchAlgorithmException, DecoderException {
        Scanner sn = new Scanner(System.in);
        
        System.out.println("Digite o texto a ser cifrado: ");
        String plainText = sn.nextLine();
        
        byte[] P = plainText.getBytes();
        
        System.out.println("Digite a senha: ");
        String password = sn.nextLine();
        
        GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());

        // Deriva a chave
        String derivedKey = generateDerivedKey(password, getSalt(), MAC_SIZE);
        
        System.out.println("Chave derivada: " + derivedKey);
        
        byte[] K = Hex.decodeHex(derivedKey);
        
        KeyParameter key2 = new KeyParameter(K);
        
        // Gera o IV
        byte[] N = new byte[12];
        new SecureRandom().nextBytes(N);
        
        System.out.println("IV: " + Utils.toHex(N));
        
        AEADParameters params = new AEADParameters(key2, MAC_SIZE, N);

        gcm.init(true, params);
        int outsize = gcm.getOutputSize(P.length);
        byte[] outc = new byte[outsize];
        //processa os bytes calculando o offset para cifrar
        int lengthOutc = gcm.processBytes(P, 0, P.length, outc, 0);

        try {
            //cifra os bytes
            gcm.doFinal(outc, lengthOutc);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }

        System.out.println("Texto cifrado: " + Utils.toHex(outc));
        
        Utils.requestEnter();
    }
    
    private static String generateDerivedKey(String password, String salt, Integer iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, 128);
        SecretKeyFactory pbkdf2 = null;
        String derivedPass = null;
        try {
            pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            SecretKey sk = pbkdf2.generateSecret(spec);
            derivedPass = Hex.encodeHexString(sk.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return derivedPass;
    }
    
    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }
}