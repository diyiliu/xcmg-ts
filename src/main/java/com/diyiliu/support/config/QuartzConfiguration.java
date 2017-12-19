package com.diyiliu.support.config;

import com.diyiliu.web.dao.OperateDao;
import com.diyiliu.web.model.TableSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: QuartzConfiguration
 * Author: DIYILIU
 * Update: 2017-12-18 14:34
 */

@Component
public class QuartzConfiguration {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OperateDao operateDao;


    @Scheduled(fixedDelay = 6 * 3600 * 1000, initialDelay = 20 * 1000)
    @CachePut(value = "xcmg-tablespace", key = "'sdb'")
    public List<TableSpace> refreshServiceTableSpace() {
        logger.info("刷新业务数据库表空间信息...");

        return operateDao.queryTableSpaces("sdb");
    }

    @Scheduled(fixedDelay = 12 * 3600 * 1000, initialDelay = 10 * 1000)
    @CachePut(value = "xcmg-tablespace", key = "'cdb'")
    public List<TableSpace> refreshCoreTableSpace() {
        logger.info("刷新核心数据库表空间信息...");

        return operateDao.queryTableSpaces("cdb");
    }
}
