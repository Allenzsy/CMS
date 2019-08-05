package com.zsy.cms.backend.dao;

import com.zsy.cms.backend.model.Admin;

public interface AdminDao {

    public Admin findAdminByUsername(String username);

    public void addAdmin(Admin admin);

}
