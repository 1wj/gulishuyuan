package com.iweb.demo.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iweb.commonutils.R;
import com.iweb.demo.eduservice.entity.EduTeacher;
import com.iweb.demo.eduservice.entity.vo.TeacherQuery;
import com.iweb.demo.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-11-18
 */
@Api(description="讲师管理")//swagger中文注释可以不要
@RestController
@RequestMapping("/eduservice/teacher")
//@CrossOrigin   //解决跨域问题
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")//swagger中文注释可以不要
    @GetMapping("/findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = teacherService.list(null);

        return R.ok().data("items",list);

    }


    //逻辑删除
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)//swagger中文注释可以不要
            @PathVariable("id") String id){
        boolean b = teacherService.removeById(id);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //分页查询讲师的方法
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageList(@PathVariable("current") Long current,
                      @PathVariable("limit") long limit){

        //创建page对象
        Page<EduTeacher> pageTeacher =new Page<>(current,limit);

        teacherService.page(pageTeacher, null);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //条件查询带分页
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherConditon(@PathVariable("current") Long current,
                                 @PathVariable("limit") long limit,
                                 @RequestBody(required = false) TeacherQuery teacherQuery){
        /*
          @RequestBody使用json传递数据，把json数据封装到对应对象里面
          若使用它 提交方式的改成post  get取不到数据
          required = false 表示 参数值可以为空
         */

        //创建page对象
        Page<EduTeacher> pageTeacher =new Page<>(current,limit);

        //创建wrapper对象 构建条件
        QueryWrapper<EduTeacher> queryWrapper=new QueryWrapper<>();

        //多条件组合查询
        String name = teacherQuery.getName();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        Integer level = teacherQuery.getLevel();

        //判断条件是否为空
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            queryWrapper.ge("gmt_create",begin); //传的是字段名称不是属性名称而自动注入里面写的是属性名称不要混淆
        }
        if(!StringUtils.isEmpty(end)){
            queryWrapper.le("gmt_modified",end);
        }

        //排序
        queryWrapper.orderByDesc("gmt_create");


        teacherService.page(pageTeacher, queryWrapper);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据list集合

        return R.ok().data("total",total).data("rows",records);
    }

    //添加讲师得方法
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if (save)
            return R.ok();
        else
            return R.error();
    }

    //查询讲师得方法
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable("id") String id){
        EduTeacher eduTeacher = teacherService.getById(id);

            return R.ok().data("teacher",eduTeacher);
    }

    //修改讲师得方法
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = teacherService.updateById(eduTeacher);
        if (flag)
            return R.ok();
        else
            return R.error();
    }

}

