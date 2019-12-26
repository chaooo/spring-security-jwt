package com.exam.mapper;

import org.apache.ibatis.annotations.Param;

import com.exam.entity.Admin;

public interface AdminDao {

    public Admin findByNameAndPassword(@Param("name") String name, @Param("password") String password);

}


