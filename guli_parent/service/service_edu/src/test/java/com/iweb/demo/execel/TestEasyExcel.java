package com.iweb.demo.execel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    //写操作
    @Test
    public void t1(){
        //实现Excel写的操作
        //1.设置写入文件夹地址和Excel文件名称
        String fileName="E:/write.xlsx";

        //2.调用easyExcel中的write方法
        EasyExcel.write(fileName,DemoData.class).sheet("学生列表").doWrite(getDate());

    }

    private  List<DemoData> getDate() {
        List<DemoData> list=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data=new DemoData();
            data.setName("lucy"+i);
            data.setSno(i);
            list.add(data);
        }
        return list;
    }

    //读操作 与写相比就多了一个监听器
    @Test
    public void t2(){
        //实现Excel读的操作
        //1.设置读入文件夹地址和Excel文件名称
        String fileName="E:/write.xlsx";

        //2.调用easyExcel中的write方法
        EasyExcel.read(fileName,DemoData.class,new ExcelListener()).sheet().doRead();

    }
}
