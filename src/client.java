import java.net.*;
import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Signature;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
public class client
{
    static String DSA(String s) throws Exception
    {
        Signature sign = Signature.getInstance("SHA256withRSA");

        //Creating KeyPair generator object
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

        //Initializing the key pair generator
        keyPairGen.initialize(2048);

        //Generate the pair of keys
        KeyPair pair = keyPairGen.generateKeyPair();

        //Getting the public key from the key pair
        PublicKey publicKey = pair.getPublic();

        //Creating a Cipher object
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        //Initializing a Cipher object
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        //Add data to the cipher
        byte[] input = s.getBytes();
        cipher.update(input);

        //encrypting the data
        byte[] cipherText = cipher.doFinal();
        System.out.println( new String(cipherText, "UTF8"));

        //Initializing the same cipher for decryption
        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());

        //Decrypting the text
        byte[] decipheredText = cipher.doFinal(cipherText);
        String decryp= new String(decipheredText);
        return decryp;
    }

    public static void main(String args[]) throws Exception
    {
        Socket sk=new Socket("127.0.0.1",2000);
        BufferedReader sin=new BufferedReader(new InputStreamReader(sk.getInputStream()));
        PrintStream sout=new PrintStream(sk.getOutputStream());
        BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
        String s;
        System.out.println("Enter The Unique Code");
        String unique= stdin.readLine();
        sout.println(unique);
        //System.out.println(unique);
        while ( true )
        {
            System.out.print("Client : ");
            s=stdin.readLine();
            //String enc = DSA(s);
            sout.println(s);
            s=sin.readLine();
            String dec=DSA(s);
            System.out.print("Server : "+dec+"\n");
            if ( s.equalsIgnoreCase("BYE") )
                break;
        }
        sk.close();
        sin.close();
        sout.close();
        stdin.close();
    }
}
