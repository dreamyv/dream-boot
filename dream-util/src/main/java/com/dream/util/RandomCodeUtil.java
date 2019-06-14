package com.dream.util;

import java.util.Random;

/**
 * 随机数生成工具，
 */
public class RandomCodeUtil {
	
	private final static Random random=new Random();
	/**
	 * 62个字母和数字，含大小写
	 */
	public static final char[]						N60_CHARS	= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

	public static String generateRandomCode(int length){
        final int maxNum = N60_CHARS.length;
        StringBuffer code = new StringBuffer("");
        int i,count=0;
        while(count < length){
            i = Math.abs(random.nextInt(maxNum));
            if (i >= 0 && i < maxNum) {
                code.append(N60_CHARS[i]);
                count ++;
            }
        }
        return code.toString();
    }

}
