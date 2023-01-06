import java.net.*;
import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Signature;
import javax.crypto.Cipher;
public class server
{
    static String RSA(String s) throws Exception
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
        ServerSocket ss=new ServerSocket(2000);
        Socket sk=ss.accept();
        BufferedReader cin=new BufferedReader(new InputStreamReader(sk.getInputStream()));
        PrintStream cout=new PrintStream(sk.getOutputStream());
        BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
        String s;
        System.out.println("Enter The Unique Code");
        String uni= cin.readLine();

        String unique= stdin.readLine();
        if(unique.equals(uni)) {
            while (true) {
                s = cin.readLine();
                if (s.equalsIgnoreCase("BYE")) {
                    cout.println("BYE");
                    break;
                }
                String dec = RSA(s);
                System.out.print("User 1 : " + dec + "\n");
                //System.out.println("User 1 : " + s);


                System.out.print("User 2 : ");
                s = stdin.readLine();
                cout.println(s);
            }
        }

        else
        {
            System.out.println("Can't Create Connection");
        }
        ss.close();
        sk.close();
        cin.close();
        cout.close();
        stdin.close();
    }
}
