package cn.aghost.nastoolserver.mapper;

import cn.aghost.nastoolserver.object.entity.EmailCheck;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

public interface EmailCheckMapper {
  int deleteByPrimaryKey(Long id);

  int insert(EmailCheck record);

  int insertSelective(EmailCheck record);

  EmailCheck selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(EmailCheck record);

  int updateByPrimaryKey(EmailCheck record);

  @Select("select * from `email_check` where status_code=0 order by id desc limit 500")
  @ResultMap("cn.aghost.nastoolserver.mapper.EmailCheckMapper.BaseResultMap")
  List<EmailCheck> selectNeedSendData();

  @ResultMap("cn.aghost.nastoolserver.mapper.EmailCheckMapper.BaseResultMap")
  @Select("select * from `email_check` where `code`=#{code}")
  EmailCheck findByCode(@Param("code") @NotBlank String code);

  @ResultMap("cn.aghost.nastoolserver.mapper.EmailCheckMapper.BaseResultMap")
  @Select("select * from `email_check` where `uid`=#{uid} order by id desc limit 50")
  List<EmailCheck> selectByUid(@Param("uid") @Positive long uid);

  int updateSentData(@Param("date") Date date, @Param("idList") List<Long> idList);

  int updateSendFailData(@Param("date") Date date, @Param("idList") List<Long> idList);

  int updateTimeout(@Param("date") Date date);
}
