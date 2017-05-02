package com.afmobi.palmcall.dao;

import com.afmobi.palmcall.model.CallRecord;
import com.jtool.db.mysql.annotation.Table;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Table(tableName = "callrecord", primaryKeyName = "id")
public class CallRecordDAO extends AbstractPalmcallDAO {

    @Override
    protected RowMapper<?> makeRowMapperInstance() {
        return (rs, rowNum) -> {
            CallRecord o = new CallRecord();
            o.setId(rs.getInt("id"));
            o.setAfid(rs.getString("afid"));
            o.setReceiverafid(rs.getString("receiverafid"));
            o.setMinutes(rs.getInt("minutes"));
            o.setStatus(rs.getInt("status"));
            o.setAddtime(new Date(rs.getTimestamp("addtime").getTime()));

            return o;
        };
    }

    @Async("dbExecutor")
    public void addCallRecord(CallRecord callRecord) {
        add(callRecord);
    }

    public List<CallRecord> selectCallRecordList(String afid, int start, int limit, int status) {
        if (status == 99) {
            return select().where("afid = ?", afid).orderByDesc("addtime").limit(start, limit).execAsList();
        }

        return select().where("afid = ? and status = ?", afid, status).
                orderByDesc("addtime").limit(start, limit).execAsList();
    }

}
