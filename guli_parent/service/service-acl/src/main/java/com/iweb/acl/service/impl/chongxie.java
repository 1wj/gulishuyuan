package com.iweb.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iweb.acl.entity.Permission;
import com.iweb.acl.mapper.PermissionMapper;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class chongxie extends ServiceImpl<PermissionMapper, Permission> {

    //-----------------递归查询----------------------------------------------
    private  List<Permission> digui(){
        //先查出所有菜单
        List<Permission> permissions = baseMapper.selectList(null);
        List<Permission> permissionList=ruKou(permissions);
        return permissionList;
    }

    private List<Permission> ruKou(List<Permission> permissions) {
        List<Permission> finalList=new ArrayList<>();//最终返回的集合
        for (Permission permissionOne : permissions) {
            if ("0".equals(permissionOne.getPid())){
                //说明是一级节点
                permissionOne.setLevel(1);

                finalList.add(xunHuan(permissionOne,permissions));
            }

        }
        return finalList;//这个返回看看要返回什么
    }

    private Permission xunHuan(Permission permissionOne, List<Permission> permissions) {
        permissionOne.setChildren(new ArrayList<>());
        for (Permission permissionTwo:permissions) {
            if(permissionOne.getId().equals(permissionTwo.getPid())){
                //说明是子节点
                permissionTwo.setLevel(permissionOne.getLevel()+1);
                if (permissionTwo.getChildren()==null){
                    permissionTwo.setChildren(new ArrayList<>());
                }
                permissionOne.getChildren().add(xunHuan(permissionTwo,permissions));
            }

        }
        return permissionOne;

    }

    //-------------------递归删除--------------------------------------

    private void  shanChu(String id){
        ArrayList fin=new ArrayList();
        qiShi(fin,id);
        fin.add(id);
        baseMapper.deleteBatchIds(fin);
    }

    private void qiShi(ArrayList fin,String id) {
        QueryWrapper<Permission> wrapper=new QueryWrapper();
        wrapper.eq("pid",id);
        wrapper.select("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);
        permissionList.forEach(item->{
            fin.add(item.getId());
            qiShi(fin,item.getId());
        });

    }
}
