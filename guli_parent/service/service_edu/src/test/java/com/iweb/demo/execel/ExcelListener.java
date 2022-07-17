package com.iweb.demo.execel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;


//监听器固定写法 读操作
public class ExcelListener extends AnalysisEventListener<DemoData> {

   //一行一行进行读取 ，表头除外
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("**"+demoData);
    }

    //读取表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头"+headMap);
    }

    //读取完成之后 目前用不到
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

}
