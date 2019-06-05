package com.dream.util;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.*;

@SuppressWarnings("all")
public class ByteUtil {
    private final static char[] mChars = "0123456789ABCDEF".toCharArray();
    private final static String mHexStr = "0123456789ABCDEF";

    /**
     * 将字节转换为无符号的整形
     * @param source
     * @return
     */
    public static int byteToUnsignedInt(byte source){
        return ((int)source)&0xFF;
    }

    /**
     * 比较两个List对象值是否相同
     * @param <T>
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> boolean compareList(List<T> a, List<T> b) {
        if(a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for(int i=0;i<a.size();i++){
            if(!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    /**
     * 检查16进制字符串是否有效
     * @param sHex String 16进制字符串
     * @return boolean
     */
    public static boolean checkHexStr(String sHex){
        String sTmp = sHex.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        int iLen = sTmp.length();

        if (iLen > 1 && iLen%2 == 0){
            for(int i=0; i<iLen; i++)
                if (!mHexStr.contains(sTmp.substring(i, i+1)))
                    return false;
            return true;
        }
        else
            return false;
    }

    /**
     * 字符串转换成十六进制字符串
     * @param str String 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str){
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        for (int i = 0; i < bs.length; i++){
            sb.append(mChars[(bs[i] & 0xFF) >> 4]);
            sb.append(mChars[bs[i] & 0x0F]);
        }
        return sb.toString().trim();
    }

    /**
     * 十六进制字符串转换成 ASCII字符串
     * @param hexStr String Byte字符串
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr){
        hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int iTmp = 0x00;;

        for (int i = 0; i < bytes.length; i++){
            iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;
            iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (iTmp & 0xFF);
        }
        return new String(bytes);
    }
    /**
     * String的字符串转换成unicode的String
     * @param strText String 全角字符串
     * @return String 每个unicode之间无分隔符
     * @throws Exception
     */
    public static String strToUnicode(String strText)
            throws Exception
    {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++){
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
                str.append("\\u");
            else // 低位在前面补00
                str.append("\\u00");
            str.append(strHex);
        }
        return str.toString();
    }

    /**
     * unicode的String转换成String的字符串
     * @param hex String 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     * ByteUtil.unicodeToString("\\u0068\\u0065\\u006c\\u006c\\u006f")
     */
    public static String unicodeToString(String hex){
        int t = hex.length() / 6;
        int iTmp = 0;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++){
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 将16进制的string转为int
            iTmp = (Integer.valueOf(s.substring(2, 4), 16) << 8) | Integer.valueOf(s.substring(4), 16);
            // 将int转换为字符
            str.append(new String(Character.toChars(iTmp)));
        }
        return str.toString();
    }

    /**
     * 将字节数组转换为指定长度的新数组,新数组长度大于原始数组时，在原始数组后补为空
     * @param source 字节数组长度应该小于等于targetBytesLength
     * @param targetBytesLength
     * @return
     */
    public static byte [] toFixedBytesPaddingEnd(byte[] source, int targetBytesLength){
        if(source.length>targetBytesLength){
            //throw new GBMessageException("source array length can not longer than target array length");
        }
        byte [] fixedBytes = new byte[targetBytesLength];
        for(int i=0;i<source.length;i++){
            fixedBytes[i] = source[i];
        }
        return fixedBytes;
    }

    /**
     * 将字节数组转换为指定长度的新数组,新数组长度大于原始数组时，在原始数组前补为空
     * @param source 字节数组长度应该小于等于targetBytesLength
     * @param targetBytesLength
     * @return
     */
    public static byte [] toFixedBytesPaddingStart(byte[] source, int targetBytesLength){
        if(source.length>targetBytesLength){
            //throw new GBMessageException("source array length can not longer than target array length");
        }
        byte [] fixedBytes = new byte[targetBytesLength];
        for(int i=(targetBytesLength-source.length);i<source.length;i++){
            fixedBytes[i] = source[i];
        }
        return fixedBytes;
    }

    /**
     * 16进制转字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase().replace(" ","");
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 字符转换为字节
     * @param c
     * @return
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 字符数组转换为字节数组
     * @param chars
     * @return
     */
    private byte[] charsToBytes (char[] chars) {
        Charset cs = Charset.forName ("UTF-8");
        CharBuffer cb = CharBuffer.allocate (chars.length);
        cb.put (chars);
        cb.flip ();
        ByteBuffer bb = cs.encode (cb);

        return bb.array();

    }

    /**
     * 字节数组转换为字符数组
     * @param bytes
     * @return
     */
    private char[] bytesToChars (byte[] bytes) {
        Charset cs = Charset.forName ("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate (bytes.length);
        bb.put (bytes);
        bb.flip ();
        CharBuffer cb = cs.decode (bb);

        return cb.array();
    }


    /**
     * 字符转换为自己数组
     * @param c
     * @return
     */
    public static byte[] charToBytes(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    /**
     * 字节数组转换为字符
     */
    public static char bytesToChar(byte[] b) {
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        return c;
    }

    /**
     * 字节数组转换为16进制字符串
     */
    public static String byteToHexString(byte src){
        byte [] bytes = new byte[1];
        bytes[0] = src;
        return bytesToHexString(bytes,true);
    }

    /**
     * 字节数组转换为16进制字符串
     */
    public static String bytesToHexString(byte[] src,boolean space){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            if(space){
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * 字节数组转换为16进制字符串
     */
    public static String bytesToHexString(byte[] src){
        return bytesToHexString(src,false);
    }

    /**
     * 转换short为byte,网络字节序
     */
    public static byte [] shortToBytes(short s) {
        byte [] bytes = new byte[2];
        bytes[1] = (byte) (s >> 8);
        bytes[0] = (byte) (s >> 0);
        return bytes;
    }
    /**
     * 转换Int为byte，网络字节序
     */
    public static byte [] intToBytes(int s) {
        byte [] bytes = new byte[4];
        bytes[3] = (byte) (s >> 24);
        bytes[2] = (byte) (s >> 16);
        bytes[1] = (byte) (s >> 8);
        bytes[0] = (byte) (s >> 0);
        return bytes;
    }

    /**
     * 通过byte数组取到short
     *
     * @param b
     * @param offset 第几位开始取
     * @return
     */
    public static short bytesToUnsignedShort(byte[] b, int offset) {
        short i = 0;
        i |=b[0+offset]&0xFF;
        i <<=8;
        i |=b[1+offset]&0xFF;
        return (short) (i&0xFF);
    }

    /**
     * 从ByteBuffer中读取两个字节，采用网络字节序转换数据为Short
     *
     * @param bf
     * @return
     */
    public static short readToShort(ByteBuffer bf) {
        byte [] bytes = new byte[2];
        bf.get(bytes);
        return (short) (((bytes[1] << 8) | bytes[0] & 0xff));
    }

    /**
     * 转换int为byte数组
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putInt(byte[] bb, int x, int index) {
        bb[index + 3] = (byte) (x >> 24);
        bb[index + 2] = (byte) (x >> 16);
        bb[index + 1] = (byte) (x >> 8);
        bb[index + 0] = (byte) (x >> 0);
    }

    /**
     * 通过byte数组取到int
     *
     * @param bb
     * @param index
     *            第几位开始
     * @return
     */
    public static int getInt(byte[] bb, int index) {
        return (int) ((((bb[index + 3] & 0xff) << 24)
                | ((bb[index + 2] & 0xff) << 16)
                | ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));
    }

    /**
     * 转换long型为byte数组
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putLong(byte[] bb, long x, int index) {
        bb[index + 7] = (byte) (x >> 56);
        bb[index + 6] = (byte) (x >> 48);
        bb[index + 5] = (byte) (x >> 40);
        bb[index + 4] = (byte) (x >> 32);
        bb[index + 3] = (byte) (x >> 24);
        bb[index + 2] = (byte) (x >> 16);
        bb[index + 1] = (byte) (x >> 8);
        bb[index + 0] = (byte) (x >> 0);
    }

    /**
     * 通过byte数组取到long
     *
     * @param bb
     * @param index
     * @return
     */
    public static long getLong(byte[] bb, int index) {
        return ((((long) bb[index + 7] & 0xff) << 56)
                | (((long) bb[index + 6] & 0xff) << 48)
                | (((long) bb[index + 5] & 0xff) << 40)
                | (((long) bb[index + 4] & 0xff) << 32)
                | (((long) bb[index + 3] & 0xff) << 24)
                | (((long) bb[index + 2] & 0xff) << 16)
                | (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));
    }

    /**
     * 字符到字节转换
     *
     * @param ch
     * @return
     */
    public static void putChar(byte[] bb, char ch, int index) {
        int temp = (int) ch;
        // byte[] b = new byte[2];
        for (int i = 0; i < 2; i ++ ) {
            bb[index + i] = new Integer(temp & 0xff).byteValue(); // 将最高位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
    }

    /**
     * 字节到字符转换
     *
     * @param b
     * @return
     */
    public static char getChar(byte[] b, int index) {
        int s = 0;
        if (b[index + 1] > 0)
            s += b[index + 1];
        else
            s += 256 + b[index + 0];
        s *= 256;
        if (b[index + 0] > 0)
            s += b[index + 1];
        else
            s += 256 + b[index + 0];
        char ch = (char) s;
        return ch;
    }

    /**
     * float转换byte
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putFloat(byte[] bb, float x, int index) {
        // byte[] b = new byte[4];
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = new Integer(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param b
     * @param index
     * @return
     */
    public static float getFloat(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * double转换byte
     *
     * @param bb
     * @param x
     * @param index
     */
    public static void putDouble(byte[] bb, double x, int index) {
        // byte[] b = new byte[8];
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = new Long(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param b
     * @param index
     * @return
     */
    public static double getDouble(byte[] b, int index) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }

    public static byte getBCC(byte[] data) {
        byte BCC[]= new byte[1];
        for(int i=0;i<data.length;i++)
        {
            BCC[0]=(byte) (BCC[0] ^ data[i]);
        }
        return BCC[0];
    }
    public static byte getPreBCC(byte[] data) {
        byte BCC[]= new byte[1];
        for(int i=0;i<data.length-1;i++)
        {
            BCC[0]=(byte) (BCC[0] ^ data[i]);
        }
        return BCC[0];
    }

    public static byte getCheckSum(byte[] data) {
        byte checkSum = 0;
        for (int i=0; i < data.length; i++) {
            checkSum ^= data[i];
        }
        return checkSum;
    }

    public static byte[] join(byte[] data1,byte[] data2){
        if (data1 == null || data2 == null){
            return null;
        }
        byte[] data = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data, 0, data1.length);
        System.arraycopy(data2, 0, data, data1.length, data2.length);
        return data;
    }

    public static int binaryToInt(int binaryNumber){
        int decimal = 0;
        int p = 0;
        while(true){
            if(binaryNumber == 0){
                break;
            } else {
                int temp = binaryNumber % 10;
                decimal += temp * Math.pow(2, p);
                binaryNumber = binaryNumber / 10;
                p++;
            }
        }
        return decimal;
    }

    public static int binaryToInt(long binaryNumber){
        int decimal = 0;
        int p = 0;
        while(true){
            if(binaryNumber == 0){
                break;
            } else {
                long temp = binaryNumber % 10;
                decimal += temp * Math.pow(2, p);
                binaryNumber = binaryNumber / 10;
                p++;
            }
        }
        return decimal;
    }

    /**
     * ByteBuffer转byte数组
     */
    public static byte[] decodeValue(ByteBuffer bytes) {
        int len = bytes.limit() - bytes.position();
        byte[] bytes1 = new byte[len];
        bytes.get(bytes1);
        return bytes1;
    }

    /**
     * byte数组转ByteBuffer
     */
    public static ByteBuffer encodeValue(byte[] value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(value.length);
        byteBuffer.clear();
        byteBuffer.get(value, 0, value.length);
        return byteBuffer;
    }

    /**
     * 字节类型转16进制
     */
    public static String byteToHex(byte bs[]) {
        StringBuffer sb = new StringBuffer();
        if (bs != null && bs.length > 0) {
            for (int i = 0; i < bs.length; i++)
                sb.append(byteToHex(bs[i]));
        }
        return sb.toString();
    }

    /**
     * 字节类型转16进制
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xff);
        if (hex.length() == 1)
            hex = (new StringBuilder(String.valueOf('0'))).append(hex).toString();
        return hex.toUpperCase();
    }

    /**
     * 国标时间数组转long值
     */
    public static Long gbTimeArrayLong(byte[] bytes1){
        int year = Short.toUnsignedInt(bytes1[0])+2000;
        int month = Byte.toUnsignedInt(bytes1[1]);
        int day = Byte.toUnsignedInt(bytes1[2]);
        int hour = Byte.toUnsignedInt(bytes1[3]);
        int minute = Byte.toUnsignedInt(bytes1[4]);
        int seconds = Byte.toUnsignedInt(bytes1[5]);
        Date date = DateUtil.parseDate(year, month, day, hour, minute, seconds);
        return date.getTime();
    }

    /**
     * ByteBuffer转HexString
     */
    public static String byteBufferToHexString(ByteBuffer byteBuffer) {
        byte[] bytes = ByteUtil.decodeValue(byteBuffer);
        return bytesToHexString(bytes);
    }

    /**
     * long值转国标时间数组
     */
    public static byte[] longToGbTime(Long time){
        Calendar calendar = longToCalendar(time);
        byte[] bytes = CreateDateTimeBytes(calendar);
        return bytesToGBDataTime(bytes);
    }

    /**
     * long值转Calendar对象
     */
    public static Calendar longToCalendar(Long time){
        Date date=new Date(time);
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    public static byte[] CreateDateTimeBytes(Calendar datetime) {
        byte[] Reply = new byte[7];
        Reply[0] = (byte) ((datetime.get(Calendar.YEAR) & 0xFF00) >> 8);
        Reply[1] = (byte) (datetime.get(Calendar.YEAR) & 0xFF);
        Reply[2] = (byte) (datetime.get(Calendar.MONTH)+1);
        Reply[3] = (byte) (datetime.get(Calendar.DAY_OF_MONTH));
        Reply[4] = (byte) (datetime.get(Calendar.HOUR_OF_DAY));
        Reply[5] = (byte) (datetime.get(Calendar.MINUTE));
        Reply[6] = (byte) (datetime.get(Calendar.SECOND));
        return Reply;
    }

    //转成国标时间
    public static byte[] bytesToGBDataTime(byte[] dataTimebytes) {
        //国标时间0.year 1.month 2.day 3.hour 4.min 5.second
        byte[] gbTime = new byte[6];
        byte[] yearByte = { 0x00, 0x00 };
        int yearInt = 0;
        if(dataTimebytes.length==7) {
            System.arraycopy(dataTimebytes, 0, yearByte, 0, 2);
            yearInt = ByteUtil.getUnsignedInt(yearByte)-2000;
            yearByte = ByteUtil.short2Byte((short)yearInt);
            gbTime[0] = yearByte[1];
            System.arraycopy(dataTimebytes, 2, gbTime, 1, 1);
            System.arraycopy(dataTimebytes, 3, gbTime, 2, 1);
            System.arraycopy(dataTimebytes, 4, gbTime, 3, 1);
            System.arraycopy(dataTimebytes, 5, gbTime, 4, 1);
            System.arraycopy(dataTimebytes, 6, gbTime, 5, 1);
            return gbTime;
        } else{
            return null;
        }
    }

    public static int getUnsignedInt(byte bytes[]) {
        return byte2Short(bytes).shortValue() & 0xffff;
    }

    public static byte[] short2Byte(short x) {
        byte bb[] = new byte[2];
        bb[0] = (byte) (x >> 8);
        bb[1] = (byte) (x >> 0);
        return bb;
    }

    public static Short byte2Short(byte bb[]) {
        if (bb != null && bb.length == 2) {
            ByteBuffer aa = ByteBuffer.wrap(bb);
            return Short.valueOf(aa.getShort());
        } else {
            return null;
        }
    }

}
