package com.jingcheng.user.mapper;

import com.jingcheng.user.entity.Role;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysRoleMapper extends Mapper<Role> {

    @Select("SELECT DISTINCT r.code,r.name,r.description" +
            " FROM  users_roles_relation urr" +
            " INNER JOIN roles r ON r.id = urr.role_id" +
            " WHERE urr.user_id = #{userId}")
    List<Role> getRoleByUserId(Long userId);
}