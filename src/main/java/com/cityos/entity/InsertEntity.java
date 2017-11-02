package com.cityos.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>title:</p> <p>description:</p>
 *
 * @author yangqc
 * @date Created in 2017-11-02
 * @modified By yangqc
 */
@Data
@AllArgsConstructor
public class InsertEntity {

  public InsertEntity() {
  }

  /**
   * id
   */
  private int id;

  /**
   * 中文名
   */
  private String chsName;

  /**
   * 英文名
   */
  private String enName;

  /**
   * 生日
   */
  private Date birthday;

  /**
   * 时间戳
   */
  private long timestamp;

  /**
   * 年龄
   */
  private int age;

  /**
   * 体重
   */
  private float weight;

  /**
   * 工资
   */
  private double salary;

  /**
   * 性别
   */
  private boolean sex;

  /**
   * 邮箱
   */
  private String email;

}
