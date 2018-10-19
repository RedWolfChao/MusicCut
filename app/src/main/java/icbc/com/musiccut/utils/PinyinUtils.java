package icbc.com.musiccut.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created By RedWolf on 2018/10/12 14:43
 * PinyinUtils.java
 */

public class PinyinUtils {

    /**
     * 将汉字转换为全拼
     *
     * @param src xx
     * @return xx
     */
    public static String getPinYin(String src) {
        char[] hz;
        hz = src.toCharArray();//该方法的作用是返回一个字符数组，该字符数组中存放了当前字符串中的所有字符
        String[] py;//该数组用来存储
        //设置汉子拼音输出的格式
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        StringBuilder pys = new StringBuilder(); //存放拼音字符串

        try {
            for (char aHz : hz) {
                //先判断是否为汉字字符
                if (Character.toString(aHz).matches("[\\u4E00-\\u9FA5]+")) {
                    //将汉字的几种全拼都存到py数组中
                    py = PinyinHelper.toHanyuPinyinStringArray(aHz, format);
                    //取出改汉字全拼的第一种读音，并存放到字符串pys后
                    pys.append(py[0]);
                } else {
                    //如果不是汉字字符，间接取出字符并连接到 pys 后
                    pys.append(Character.toString(aHz));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return pys.toString();
    }

    /**
     * 提取每个汉字的首字母
     *
     * @param str zz
     * @return xx
     */
    public static String getPinYinHeadChar(String str) {
        StringBuilder convert = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            //提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString().toUpperCase();
    }

    /**
     * 将字符串转换成ASCII码
     */
    public static String getCnASCII(String str) {
        StringBuilder buf = new StringBuilder();
        //将字符串转换成字节序列
        byte[] bGBK = str.getBytes();
        for (byte aBGBK : bGBK) {
            //将每个字符转换成ASCII码
            buf.append(Integer.toHexString(aBGBK & 0xff));
        }
        return buf.toString();
    }
}
