package com.yangqc.dg.utils;

import java.util.Random;

/**
 * 有关Email地址相关的随机数据生成器
 *
 * @author yangqc
 */
public class RandomEmail {

  /**
   * 生成一个随机Email地址. <p>生成一个随机Email地址，该函数不能保证该地址的有效性，仅能保证其格式的准确性和数据的随机性。
   *
   * @return 一个Email地址字符串
   */
  public static String nextEmail() {
    String ret;
    final String[] suffix = new String[]{"gmail.com",
        "yahoo.com", "msn.com", "hotmail.com", "aol.com",
        "ask.com", "live.com", "qq.com", "0355.net", "163.com",
        "163.net", "263.net", "3721.net", "yeah.net",
        "googlemail.com", "126.com", "sina.com", "sohu.com",
        "yahoo.com.cn"};
    Random random = new Random();
    int index = random.nextInt(100);
    while (index >= suffix.length) {
      index = random.nextInt(100);
    }
    try {
      ret = ExtraRandom.nextRandomLengthString(1, 15, "digit_letter") + "@" + suffix[index];
    } catch (Exception e) {
      throw new RuntimeException(e.getCause());
    }
    return ret;
  }
}
