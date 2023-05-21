package com.yicj.spark.model;

import lombok.Data;

import java.util.Date;

/**
 * @author: yicj
 * @date: 2023/5/21 21:11
 */
@Data
public class Book {
    private int id;
    private int authorId;
    private String title;
    private Date releaseDate;
    private String link;

    /**
     * @param authorId
     *          the authorId to set
     */
    public void setAuthorId(Integer authorId) {
        if (authorId == null) {
            this.authorId = 0;
        } else {
            this.authorId = authorId;
        }
    }
}
