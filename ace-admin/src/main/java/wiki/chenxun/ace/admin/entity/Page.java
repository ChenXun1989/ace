package wiki.chenxun.ace.admin.entity;

import lombok.Data;

import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/23.
 */
@Data
public class Page<T> {

    private long total;

    private List<T> rows;

}
