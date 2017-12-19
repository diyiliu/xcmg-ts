package com.diyiliu.web.controller;

import com.diyiliu.web.dao.OperateDao;
import com.diyiliu.web.model.TableSpace;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: OperateController
 * Author: DIYILIU
 * Update: 2017-12-15 09:35
 */

@RestController
public class OperateController {

    @Resource
    private OperateDao operateDao;

    @Resource
    private CacheManager cacheManager;

    @GetMapping("/")
    public String index() {

        return "Check XZYWDB Tablespace";
    }

    @GetMapping("/list/{db}")
    public List<TableSpace> spaceList(@PathVariable String db) {
        Cache xcmgCache = cacheManager.getCache("xcmg-tablespace");
        List list = xcmgCache.get(db, List.class);
        if (list == null) {
            list = operateDao.queryTableSpaces(db);
        }

        return list;
    }
}
