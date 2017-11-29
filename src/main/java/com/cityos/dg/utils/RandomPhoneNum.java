package com.cityos.dg.utils;

import java.util.Random;

/**
 * 有关电话手机号码等的随机数据生成器
 *
 * @author yangqc
 */
public class RandomPhoneNum {

  /**
   * 生成一个随机手机号码. <p>该函数不能保证手机号码的实用性，仅能保证其格式的有效性和数据的随机性。
   *
   * @return 一个随机手机号码
   */
  public static String nextPhoneNum() {
    String ret;
    final String[] suffix = new String[]{"13", "18", "15"};
    Random random = new Random();
    int index = random.nextInt(10);
    while (index >= suffix.length) {
      index = random.nextInt(10);
    }
    ret = suffix[index] + ExtraRandom.nextString(9, "digit");
    return ret;
  }

}
