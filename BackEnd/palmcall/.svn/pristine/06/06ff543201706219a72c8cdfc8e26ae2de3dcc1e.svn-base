package com.afmobi.palmcall.dao;

import com.afmobi.palmcall.model.UserHot;
import com.jtool.db.mysql.annotation.Table;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Table(tableName = "userhot", primaryKeyName = "id")
public class UserHotDAO extends AbstractPalmcallDAO {

    @Override
    protected RowMapper<?> makeRowMapperInstance() {
        return (rs, rowNum) -> {
            UserHot o = new UserHot();
            o.setId(rs.getInt("id"));
            o.setAfid(rs.getString("afid"));
            o.setAddtime(new Date(rs.getTimestamp("addtime").getTime()));

            return o;
        };
    }

    public void addUserHot(UserHot userHot) {
        add(userHot);
    }

    public boolean hasUserHot(String afid) {
        return select().where("afid = ?", afid).hasRecord();
    }

    public void deleteUserHot(String afid) {
        select().where("afid = ?", afid).delete();
    }

    public List<String> selectUserHotList() {
        List<String> data = new ArrayList<>();

        List<UserHot> list = select().execAsList();
        for (UserHot u : list) {
            data.add(u.getAfid());
        }

        return data;
    }

}
