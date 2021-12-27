package com.example.jwt.entity;

import com.github.pagehelper.Page;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/16
 */
@Data
public class PaginationVo<T> implements Serializable {
    /** 当前页码 */
    @Builder.Default
    private int pageNum = 1;
    /** 每页大小 */
    @Builder.Default
    private int pageSize = 10;
    /** 总记录数 */
    private int totalRecord;
    /** 总页数 */
    private int totalPage;
    /** 是否有后一页 */
    private boolean hasNextPage;
    /** 是否有前一页 */
    private boolean hasPreviousPage;
    /** 查询结果 */
    List<T> results;

    public PaginationVo(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.totalRecord = (int) page.getTotal();
            this.totalPage = page.getPages();
            setHasPreviousAndNext();
            this.results = page;
        } else if (list != null) {
            this.pageSize = list.size();
            this.totalRecord = list.size();
            this.totalPage = 1;
            this.results = list;
        }
    }

    public PaginationVo(int pageNum, int pageSize, int totalRecord, List<T> results) {
        super();
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        this.totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        this.results = results;
        hasPreviousPage = this.pageNum >= 2;
        hasNextPage = this.pageNum < totalPage;
    }

    public PaginationVo(int pageNum, int maxSize, int totalRecord, int totalPage,
                        List<T> results) {
        super();
        this.pageNum = pageNum;
        this.pageSize = maxSize;
        this.totalRecord = totalRecord;
        this.totalPage = totalPage;
        this.results = results;
        hasPreviousPage = this.pageNum >= 2;
        hasNextPage = this.pageNum < totalPage;
    }

    public PaginationVo() {super();}

    public void setHasPreviousAndNext() {
        hasPreviousPage = this.pageNum >= 2;
        hasNextPage = this.pageNum < totalPage;
    }

    public int countTotalPage() {
        int total = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        this.setTotalPage(total);
        return total;
    }

    private static final long serialVersionUID = 1L;
}

