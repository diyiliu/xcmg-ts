package com.diyiliu.web.dao;

import com.diyiliu.support.task.CreateSpaceTask;
import com.diyiliu.web.model.TableSpace;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Description: OperateDao
 * Author: DIYILIU
 * Update: 2017-12-15 09:36
 */

@Repository
public class OperateDao {

    @Resource
    private JdbcTemplate serviceJdbcTemplate;

    @Resource
    private JdbcTemplate coreJdbcTemplate;

    @Resource
    private Executor xcmgExecutor;

    public List<TableSpace> queryTableSpaces(String db) {
        List list = new ArrayList();

        String suffix = "";
        JdbcTemplate jdbcTemplate = null;
        if (db.equals("sdb")) {
            suffix = "xzywdb";
            jdbcTemplate = serviceJdbcTemplate;
        } else if (db.equals("cdb")) {
            suffix = "xzordata";
            jdbcTemplate = coreJdbcTemplate;
        }

        if (jdbcTemplate == null || StringUtils.isEmpty(suffix)) {
            return list;
        }

        String sql = "SELECT a.tablespace_name name," +
                "       a.bytes total," +
                "       b.bytes used," +
                "       c.bytes free," +
                "       (b.bytes * 100) / a.bytes usedp," +
                "       (c.bytes * 100) / a.bytes freep" +
                "  FROM sys.sm$ts_avail a, sys.sm$ts_used b, sys.sm$ts_free c" +
                " WHERE a.tablespace_name = b.tablespace_name" +
                "   AND a.tablespace_name = c.tablespace_name" +
                " ORDER BY a.tablespace_name, freep DESC";

        list = jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
            TableSpace ts = new TableSpace();
            ts.setName(rs.getString("name"));
            ts.setTotal(new BigDecimal(rs.getLong("total")).divide(new BigDecimal(1024 * 1024)).longValue());
            ts.setUsed(new BigDecimal(rs.getLong("used")).divide(new BigDecimal(1024 * 1024)).longValue());
            ts.setFree(new BigDecimal(rs.getLong("free")).divide(new BigDecimal(1024 * 1024)).longValue());

            ts.setUsedPerc(new BigDecimal(rs.getDouble("usedp")).setScale(1, BigDecimal.ROUND_DOWN).doubleValue());
            ts.setFreePerc(new BigDecimal(rs.getDouble("freep")).setScale(1, BigDecimal.ROUND_DOWN).doubleValue());
            return ts;
        });

        checkSpace(list, suffix, jdbcTemplate);

        return list;
    }

    /**
     * 校验表空间文件
     * 剩余容量
     *
     * @param list
     */
    public void checkSpace(List<TableSpace> list, String suffix, JdbcTemplate jdbcTemplate) {
        for (TableSpace ts : list) {
            int free = ts.getFreePerc().intValue();

            if (free < 1) {
                CreateSpaceTask task = new CreateSpaceTask(jdbcTemplate, ts.getName(), suffix);
                xcmgExecutor.execute(task);
            }
        }
    }
}
