package com.afmobi.palmcall.dao;

import com.afmobi.palmcall.model.Operator;
import com.jtool.db.mysql.annotation.Table;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
@Table(tableName = "operator", primaryKeyName = "id")
public class OperatorDAO extends AbstractPalmcallDAO {

    @Override
    protected RowMapper<?> makeRowMapperInstance() {
        return (rs, rowNum) -> {
            Operator o = new Operator();
            o.setId(rs.getInt("id"));
            o.setAfid(rs.getString("afid"));
            o.setAmount(rs.getInt("amount"));
            o.setCallnumber(rs.getInt("callnumber"));
            o.setLeftminutes(rs.getInt("leftminutes"));
            o.setStarttime(rs.getInt("starttime"));
            o.setEndtime(rs.getInt("endtime"));
            o.setOpen(rs.getInt("open"));
            o.setCoverImgPath(rs.getString("coverimgpath"));
            o.setCoverTs(rs.getString("coverts"));
            o.setAudioPath(rs.getString("audiopath"));
            o.setAudioTs(rs.getString("audiots"));
            o.setAudioDuration(rs.getInt("audioduration"));
            o.setSex(rs.getString("sex"));
            o.setBirthdate(rs.getString("birthdate"));
            o.setName(rs.getString("name"));
            o.setAddtime(new Date(rs.getTimestamp("addtime").getTime()));

            return o;
        };
    }

    public long addOperator(Operator operator) {
        return addAndReturnPrimaryKey(operator);
    }

    public boolean hasOperator(String afid) {
        return select().where("afid = ?", afid).hasRecord();
    }

    public Optional<Operator> getOperator(String afid) {
        return select().where("afid = ?", afid).execAsPojoOpt();
    }

    public int getLeftMinutes(String afid) {
        Optional<Operator> operator = getOperator(afid);
        if (operator.isPresent()) {
            return operator.get().getLeftminutes();
        }

        return 0;
    }

    //修改免打扰设置
    @Async("dbExecutor")
    public void updateNotDisturb(String afid, int open, int startTime, int endTime) {
        String sql = "update " + getTableName() + " set open = ?, starttime = ?, endtime = ? where afid = ?";
        jdbcTemplate.update(sql, open, startTime, endTime, afid);
    }

    //增加接听次数
    @Async("dbExecutor")
    public void updateAmount(String afid, int amount) {
        String sql = "update " + getTableName() + " set amount = amount + ? where afid = ?";
        jdbcTemplate.update(sql, amount, afid);
    }

    //增加被呼叫次数
    @Async("dbExecutor")
    public void updateCallnumber(String afid, int callnumber) {
        String sql = "update " + getTableName() + " set callnumber = callnumber + ? where afid = ?";
        jdbcTemplate.update(sql, callnumber, afid);
    }

    //初始化水军用户 接听次数 被呼叫次数
    public void initAmountAndCallnumber(String afid, int amount, int callnumber) {
        String sql = "update " + getTableName() + " set amount = ?, callnumber = ? where afid = ?";
        jdbcTemplate.update(sql, amount, callnumber, afid);
    }

    public void updateAudiopath(String afid, String audioPath, String audioTs, int audioDuration) {
        String sql = "update " + getTableName() + " set audiopath = ?, audiots = ?, audioduration = ? where afid = ?";
        jdbcTemplate.update(sql, audioPath, audioTs, audioDuration, afid);
    }

    public void updateCoverImgPath(String afid, String coverImgPath, String coverTs) {
        String sql = "update " + getTableName() + " set coverimgpath = ?, coverts = ? where afid = ?";
        jdbcTemplate.update(sql, coverImgPath, coverTs, afid);
    }

    //减少分钟数
    public int reduceLeftminutes(String afid, int minutes) {
        //客户端控制分钟数不准，有可能菊风通知的通话时长，超过剩余分钟数，直接减去，会出现负数的情况
        String sql1 = "update " + getTableName() + " set leftminutes = leftminutes - ? where afid = ? and leftminutes >= ?";
        int updateRow = jdbcTemplate.update(sql1, minutes, afid, minutes);
        if (updateRow > 0) {
            return updateRow;
        }

        String sql2 = "update " + getTableName() + " set leftminutes = 0 where afid = ?";
        return jdbcTemplate.update(sql2, afid);
    }

    //增加分钟数
    public int addLeftminutes(String afid, int minutes) {
        String sql = "update " + getTableName() + " set leftminutes = leftminutes + ? where afid = ?";
        return jdbcTemplate.update(sql, minutes, afid);
    }

}
