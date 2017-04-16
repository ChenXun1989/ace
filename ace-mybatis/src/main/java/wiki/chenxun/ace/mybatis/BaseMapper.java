package wiki.chenxun.ace.mybatis;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/15.
 */
public interface BaseMapper<T> {


    @SelectProvider(type =BaseProvider.class,method = "queryByPrimaryKey")
    T queryByPrimaryKey(T t);

    @InsertProvider(type =BaseProvider.class,method = "insert")
    long insert(T t);

    @UpdateProvider(type =BaseProvider.class,method = "update")
    long update(T t);

    @DeleteProvider(type =BaseProvider.class,method = "delete")
    long delete(Object object);

}
