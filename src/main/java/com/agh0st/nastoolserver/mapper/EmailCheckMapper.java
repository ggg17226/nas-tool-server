package com.agh0st.nastoolserver.mapper;

import com.agh0st.nastoolserver.object.entity.EmailCheck;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface EmailCheckMapper {
  int deleteByPrimaryKey(Long id);

  int insert(EmailCheck record);

  int insertSelective(EmailCheck record);

  EmailCheck selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(EmailCheck record);

  int updateByPrimaryKey(EmailCheck record);

  @Select("select * from `email_check` where status_code=0")
  @ResultMap("com.agh0st.nastoolserver.mapper.EmailCheckMapper.BaseResultMap")
  List<EmailCheck> selectNeedSendData();

  int updateSentData(@Param("date") Date date, @Param("idList") List<Long> idList);

  int updateSendFailData(@Param("date") Date date, @Param("idList") List<Long> idList);

  int updateTimeout(@Param("date") Date date);
}
