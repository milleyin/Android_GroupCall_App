package com.afmobi.palmcall.dao;

import com.afmobi.palmcall.model.NowCallUser;
import com.jtool.db.mysql.annotation.Table;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Table(tableName = "nowcalluser", primaryKeyName = "id")
public class NowCallUserDAO extends AbstractPalmcallDAO {

    @Override
    protected RowMapper<?> makeRowMapperInstance() {
        return (rs, rowNum) -> {
            NowCallUser o = new NowCallUser();
            o.setId(rs.getInt("id"));
            o.setAfid(rs.getString("afid"));
            o.setAddtime(new Date(rs.getTimestamp("addtime").getTime()));

            return o;
        };
    }

    @Async("dbExecutor")
    public void addNowCallUser(String afid) {
        NowCallUser nowCallUser = new NowCallUser();
        nowCallUser.setAfid(afid);
        nowCallUser.setAddtime(new Date());
        add(nowCallUser);
    }

    @Async("dbExecutor")
    public void deleteNowCallUser(String afid) { //删除24小时以内的用户
        select().where("afid = ? and hour(timediff(now(),addtime)) < 24", afid).delete();
    }

}
