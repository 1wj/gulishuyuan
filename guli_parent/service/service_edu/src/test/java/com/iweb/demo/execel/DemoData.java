package com.iweb.demo.execel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.sql.Struct;

@Data
public class DemoData {
    //设置Excel表头名称   前面的表示写操作中的表头， 后面表示读操作中的第一列
    @ExcelProperty(value = "学生编号",index = 0)
    private Integer sno;

    @ExcelProperty(value = "学生姓名",index = 1)
    private String name;

}
