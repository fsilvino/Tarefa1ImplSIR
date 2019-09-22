package tarefa1implsir;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;

/**
 * Esta é a solução da questão 10
 * ----------------------------------------------------------------------------
 * 10. Crie um programa que recebe duas strings pelo teclado e calcula o 
 * MAC (resumo criptográfico com chave) de cada uma das strings escrevendo o 
 * resultado na tela. Teste e explique o funcionamento do programa com entrada
 * de strings iguais e depois com entrada de strings diferentes. Compare a saída
 * obtida nessa questão com a saída obtida na questão 7.
 * ----------------------------------------------------------------------------
 * @author Flávio e Diogo
 */
public class Questao10 {

    public Questao10() {
        System.out.println("--------- Questão 10 ----------");
        try {
            resolver();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | ShortBufferException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Questao10.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resolver()  throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        int addProvider;
        addProvider = Security.addProvider(new BouncyCastleFipsProvider());
        
        Scanner sn = new Scanner(System.in);
        
        System.out.println("Digite a mensagem 1: ");
        String input1 = sn.nextLine();
        
        System.out.println("Digite a mensagem 2: ");
        String input2 = sn.nextLine();
        
        String input1Cifrado = calcularHMac(input1);
        String input2Cifrado = calcularHMac(input2);
        
        System.out.println("Mensagem 1 cifrada: " + input1Cifrado);
        System.out.println("Mensagem 2 cifrada: " + input2Cifrado);
        
        Utils.requestEnter();
    }
    
    private String calcularHMac(String input) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException  {
        SecureRandom	random = new SecureRandom();
        IvParameterSpec ivSpec = Utils.createCtrIvForAES(1, random);
        Key             key = Utils.createKeyForAES(128, random);
        Cipher          cipher = Cipher.getInstance("AES/CTR/NoPadding", "BCFIPS");
        Mac             hMac = Mac.getInstance("HMacSHA256", "BCFIPS");
        Key             hMacKey = new SecretKeySpec(key.getEncoded(), "HMacSHA256");
        
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        
        byte[] cipherText = new byte[cipher.getOutputSize(input.length() + hMac.getMacLength())];

        int ctLength = cipher.update(Utils.toByteArray(input), 0, input.length(), cipherText, 0);
        
        hMac.init(hMacKey);
        hMac.update(Utils.toByteArray(input));
        
        ctLength += cipher.doFinal(hMac.doFinal(), 0, hMac.getMacLength(), cipherText, ctLength);
        
        return Utils.toHex(cipherText, ctLength);
    }
}
