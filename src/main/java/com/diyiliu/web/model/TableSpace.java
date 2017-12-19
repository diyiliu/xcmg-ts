package com.diyiliu.web.model;

/**
 * Description: TableSpace
 * Author: DIYILIU
 * Update: 2017-12-14 16:16
 */
public class TableSpace {

    private String name;

    // 单位：M
    private Long total;
    private Long used;
    private Long free;

    private Double usedPerc;
    private Double freePerc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }

    public Long getFree() {
        return free;
    }

    public void setFree(Long free) {
        this.free = free;
    }

    public Double getUsedPerc() {
        return usedPerc;
    }

    public void setUsedPerc(Double usedPerc) {
        this.usedPerc = usedPerc;
    }

    public Double getFreePerc() {
        return freePerc;
    }

    public void setFreePerc(Double freePerc) {
        this.freePerc = freePerc;
    }
}
