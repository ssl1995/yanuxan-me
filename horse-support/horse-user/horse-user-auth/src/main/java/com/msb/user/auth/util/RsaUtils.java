package com.msb.user.auth.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * rsa非对称加密
 * 私钥加密，解密需要公钥
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年09月07日 19时15分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class RsaUtils {

    /**
     * 从文件中读取公钥
     *
     * @param filename 公钥保存路径,相对于classpath
     * @return 公钥对象
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPublicKey(bytes);
    }

    /**
     * 获取公钥
     * X.509是定义了公钥证书结构的基本标准
     *
     * @param bytes 公钥的字节形式
     * @return 公钥对象
     * @throws Exception
     */
    public static PublicKey getPublicKey(byte[] bytes) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    /**
     * 从文件中读取私钥
     *
     * @param filename 私钥保存路径，相对于classpath
     * @return 私钥对象
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }

    /**
     * 获取私钥
     *
     * @param bytes 私钥的字节形式
     * @return 私钥对象
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    /**
     * 根据密文,生成rsa公钥和私钥,并写入指定文件
     *
     * @param publicKeyFilename  公钥文件路径
     * @param privateKeyFilename 私钥文件路径
     * @param secret             生成密钥的密文
     * @throws Exception
     */
    public static void generateKey(String publicKeyFilename,
                                   String privateKeyFilename, String secret) throws Exception {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        //获取公钥并写出
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        writeFile(publicKeyFilename, publicKeyBytes);
        //获取私钥并写出
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        writeFile(privateKeyFilename, privateKeyBytes);
    }

    private static byte[] readFile(String filename) throws Exception {
        return Files.readAllBytes(new File(filename).toPath());
    }

    private static void writeFile(String destPath, byte[] bytes) throws IOException {
        File dest = new File(destPath);
        if (!dest.exists()) {
            boolean newFile = dest.createNewFile();
        }
        Files.write(dest.toPath(), bytes);
    }


}
