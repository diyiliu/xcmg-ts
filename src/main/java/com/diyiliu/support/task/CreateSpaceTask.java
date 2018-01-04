package com.diyiliu.support.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Description: CreateSpaceTask
 * Author: DIYILIU
 * Update: 2017-12-19 09:45
 */

public class CreateSpaceTask implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;

    // 表空间名称
    private String spaceName;
    // 表空间文件后缀
    private String suffix;

    public CreateSpaceTask(JdbcTemplate jdbcTemplate, String spaceName, String suffix) {
        this.jdbcTemplate = jdbcTemplate;
        this.spaceName = spaceName;
        this.suffix = suffix;
    }

    @Override
    public void run() {
        logger.info("添加[{}]表空间文件[-{}]...", spaceName, suffix);

        int count = 0;
        // 业务数据库、核心数据库
        int no = suffix.contains("yw") ? 7 : 2;
        while (true) {
            long index = getTotal(spaceName) + 1;
            long random = (int) (Math.random() * no) + 1;

            StringBuffer strb = new StringBuffer("ALTER TABLESPACE ");
            strb.append(spaceName).append(" ADD DATAFILE ");
            strb.append("'/oradb").append(random).append("/oracle/oradata/").append(suffix)
                    .append("/").append(spaceName.toLowerCase()).append(index).append(".dbf'");
            strb.append(" SIZE 1g AUTOEXTEND ON NEXT 32m MAXSIZE 30g");

            logger.warn("执行SQL:[{}]", strb.toString());
            try {
                jdbcTemplate.execute(strb.toString());
            } catch (Exception e) {
                logger.error("SQL;[{}]", strb.toString());
                e.printStackTrace();

                break;
            }

            if (++count > 2 || isSafe(spaceName)) {

                break;
            }
        }
    }

    /**
     * 获取当前表空间文件数量
     *
     * @param name
     * @return
     */
    private long getTotal(String name) {
        String sql = "SELECT COUNT(1) FROM dba_data_files t WHERE t.tablespace_name = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{name}, Long.class);
    }

    /**
     * 是否为安全剩余空间
     *
     * @param name
     * @return
     */
    private boolean isSafe(String name) {
        String sql = "SELECT (c.bytes * 100) / a.bytes freep" +
                "  FROM sys.sm$ts_avail a, sys.sm$ts_free c" +
                " WHERE a.tablespace_name = c.tablespace_name" +
                "   AND a.tablespace_name = ?";

        double free = jdbcTemplate.queryForObject(sql, new Object[]{name}, Double.class);
        if (free > 0.3) {

            return true;
        }

        return false;
    }
}
