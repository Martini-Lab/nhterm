package com.offsec.nhterm.framework.database.bean;


import com.offsec.nhterm.framework.database.DatabaseDataType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author kiva
 */
public class TableInfo {

  /**
   * 是否包含ID
   */
  public boolean containID;
  /**
   * 主键字段
   */
  public Field primaryField;

  /**
   * 表名
   */
  public String tableName;

  /**
   * 字段表
   */
  public Map<Field, DatabaseDataType> fieldToDataTypeMap;

  /**
   * 创建table的语句
   */
  public String createTableStatement;

  /**
   * 是否已经创建
   */
  public boolean isCreate = false;

  public Method afterTableCreateMethod;

}
