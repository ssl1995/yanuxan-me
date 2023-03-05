package com.msb.pay.kit;

import com.alibaba.fastjson.JSONObject;
import com.msb.framework.common.exception.BizException;
import com.msb.pay.model.BaseSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 签名工具类
 *
 * @author peng.xy
 * @date 2022/6/9
 */
@Slf4j
public class SignKit {

    private SignKit() {
    }

    /**
     * 获取map签名字符串
     *
     * @param map：签名对象map
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/6/9
     */
    private static String getStrSort(Map<String, Object> map) {
        List<String> list = new LinkedList<>();
        Iterator var2 = map.entrySet().iterator();
        while (var2.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) var2.next();
            if (null != entry.getValue() && !"".equals(entry.getValue())) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            sb.append(arrayToSort[i]);
        }
        return sb.toString();
    }

    /**
     * 获取字符串MD5
     *
     * @param value
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/6/9
     */
    private static String md5(String value) {
        MessageDigest md = null;
        try {
            byte[] data = value.getBytes(StandardCharsets.UTF_8);
            md = MessageDigest.getInstance("MD5");
            byte[] digestData = md.digest(data);
            return toHex(digestData);
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    /**
     * 获取十六进制字符串
     *
     * @param input：byte数组
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/6/9
     */
    private static String toHex(byte[] input) {
        if (input == null) {
            return null;
        } else {
            StringBuffer output = new StringBuffer(input.length * 2);
            for (int i = 0; i < input.length; ++i) {
                int current = input[i] & 255;
                if (current < 16) {
                    output.append("0");
                }
                output.append(Integer.toString(current, 16));
            }
            return output.toString();
        }
    }

    /**
     * 获取签名字符串
     *
     * @param signObject：签名对象
     * @param key：签名秘钥
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/6/9
     */
    public static String getSign(BaseSign signObject, String key) {
        JSONObject map = JSONObject.parseObject(JSONObject.toJSONString(signObject));
        map.remove("sign");
        return md5(getStrSort(map) + "key=" + key).toUpperCase();
    }

    /**
     * 将签名对象进行签名
     *
     * @param signObject：签名对象
     * @param key：签名秘钥
     * @author peng.xy
     * @date 2022/6/9
     */
    public static void setSign(BaseSign signObject, String key) {
        signObject.setTimestamp(System.currentTimeMillis());
        signObject.setSign(getSign(signObject, key));
    }

    /**
     * 校验签名
     *
     * @param signObject：签名对象
     * @param key：签名秘钥 可以是运营后台设置的一个字符串
     * @return boolean
     * @author peng.xy
     * @date 2022/6/9
     */
    public static boolean signature(BaseSign signObject, String key) {
        return StringUtils.equals(signObject.getSign(), getSign(signObject, key));
    }

    /**
     * 校验签名，失败抛出异常
     *
     * @param signObject：签名对象
     * @param key：签名秘钥
     * @return boolean
     * @author peng.xy
     * @date 2022/6/9
     */
    public static void signatureValidate(BaseSign signObject, String key) {
        boolean signature = signature(signObject, key);
        log.info("签名校验：{}", signature);
        if (!signature) {
            throw new BizException("签名校验失败");
        }
    }

}
