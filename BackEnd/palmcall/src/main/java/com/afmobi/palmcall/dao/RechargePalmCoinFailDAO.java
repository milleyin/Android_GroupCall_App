package com.afmobi.palmcall.dao;

import com.afmobi.palmcall.model.RechargePalmCoinFail;
import com.jtool.db.mysql.annotation.Table;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Table(tableName = "rechargepalmcoinfail", primaryKeyName = "id")
public class RechargePalmCoinFailDAO extends AbstractPalmcallDAO {

    @Override
    protected RowMapper<?> makeRowMapperInstance() {
        return (rs, rowNum) -> {
            RechargePalmCoinFail o = new RechargePalmCoinFail();
            o.setId(rs.getInt("id"));
            o.setAfid(rs.getString("afid"));
            o.setAmount(rs.getInt("amount"));
            o.setPhonecountrycode(rs.getString("phonecountrycode"));
            o.setAddtime(new Date(rs.getTimestamp("addtime").getTime()));

            return o;
        };
    }

    @Async("dbExecutor")
    public void addRechargePalmCoinFail(RechargePalmCoinFail rechargePalmCoinFail) {
        add(rechargePalmCoinFail);
    }

    public List<RechargePalmCoinFail> selectList() {
        return select().execAsList();
    }

    public void delete(int id) {
        deleteByPrimaryKey(id);
    }

}
