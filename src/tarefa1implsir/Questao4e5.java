package tarefa1implsir;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * Esta é a solução das questões 4 e 5
 * ----------------------------------------------------------------------------
 * 4. Crie um programa que permite ao usuário entrar com uma string pelo teclado
 * e o programa cifra a string e mostra a string cifrada na tela. O código deve
 * “sortear” uma boa chave e IV. Use o modo CTR (counter) do algoritmo AES 
 * para cifrar.
 * ----------------------------------------------------------------------------
 * 5. Crie um programa que recebe a string cifrada com o programa da questão 4 
 * (questão anterior) e decifra essa string mostrando o resultado da decifragem
 * na tela.
 * ----------------------------------------------------------------------------
 * @author Flávio e Diogo
 */
public class Questao4e5 {
    
    private Key aesKey;
    private byte[] key;
    private byte iv[];
    private IvParameterSpec ivSpec;
    private Cipher cipher;
    private SecretKeySpec secretKey;

    public Questao4e5() {
        System.out.println("--------- Questão 4 ----------");
        try {
            resolver();
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Questao4e5.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Questao4e5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void init() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        // Instancia o cipher
        cipher = Cipher.getInstance("AES/CTR/NoPadding");
           
        // Gera uma chave AES
        System.out.println("Gerando chave..."); 
        KeyGenerator sKenGen = KeyGenerator.getInstance("AES");
        aesKey = sKenGen.generateKey();
        System.out.println("Chave AES: " + Hex.encodeHexString(aesKey.getEncoded()));
        
        // Gerando o IV com SecureRandom
        System.out.println("Gerando IV..."); 
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        iv = new byte[16];
        random.nextBytes(iv);
        ivSpec = new IvParameterSpec(iv);
        System.out.println("IV: " + Hex.encodeHexString(iv));
    }

    private String encrypt(String strToEncrypt) {
        try {
            try {
                init();
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(Questao4e5.class.getName()).log(Level.SEVERE, null, ex);
            }
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
            final String encryptedString = Hex.encodeHexString(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
        }
        return null;
    }

    private String decrypt(String dec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
            byte[] embytes = {};
            try {
                embytes = Hex.decodeHex(dec.toCharArray());
            } catch (DecoderException ex) {
                Logger.getLogger(Questao4e5.class.getName()).log(Level.SEVERE, null, ex);
            }

            String decryptedString = new String(cipher.doFinal(embytes));

            return decryptedString;

        } catch (IllegalBlockSizeException | BadPaddingException e) {
            System.out.println(e);
        }
        return null;
    }

    private void resolver() throws InvalidKeyException, InvalidAlgorithmParameterException {
        String paraCifrar;

        Scanner input = new Scanner(System.in);
        System.out.println("Digite a mensagem para cifrar: ");
        paraCifrar = input.nextLine();

        System.out.println("Mensagem original: " + paraCifrar);
        String cifrada = this.encrypt(paraCifrar);
        System.out.println("Mensagem cifrada: " + cifrada);
        String decifrada = this.decrypt(cifrada);
        System.out.println("Mensagem decifrada: " + decifrada);
        
        Utils.requestEnter();
    }
    
}