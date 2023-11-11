package com.np;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class TestJasypt {

    public static void main(String[] args) {
        StandardPBEStringEncryptor pbeStringEncryptor = new StandardPBEStringEncryptor();
        // key to encrypt secrets; this secret will be needed in runtime to decrypt secrets
        pbeStringEncryptor.setPassword("Demo_2023!");
        pbeStringEncryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        pbeStringEncryptor.setIvGenerator(new RandomIvGenerator());
        // by default JASYPT output is base64 encoded
        // JCE default output is hexadecimal
        pbeStringEncryptor.setStringOutputType("HEX");

        String secretToEncrypt = "spring_cloud_password!";
        String result = pbeStringEncryptor.encrypt(secretToEncrypt);

        System.out.println(result);

        System.out.println(pbeStringEncryptor.decrypt(result));
    }
}