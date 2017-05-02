package com.afmobi.palmcall.dao;

import com.afmobi.palmcall.model.GiftMinutes;
import com.jtool.db.mysql.annotation.Table;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Table(tableName = "giftminutes", primaryKeyName = "id")
public class GiftMinutesDAO extends AbstractPalmcallDAO {

    @Override
    protected RowMapper<?> makeRowMapperInstance() {
        return (rs, rowNum) -> {
            GiftMinutes o = new GiftMinutes();
            o.setId(rs.getInt("id"));
            o.setAfid(rs.getString("afid"));
            o.setAddtime(new Date(rs.getTimestamp("addtime").getTime()));

            return o;
        };
    }

    @Async("dbExecutor")
    public void addGiftMinutes(GiftMinutes giftMinutes) {
        add(giftMinutes);
    }

    public boolean hasGiftMinutes(String afid) {
        return select().where("afid = ?", afid).hasRecord();
    }

}
