package com.agh0st.nastoolserver.object.PO;

import java.io.Serializable;
import java.util.*;

/** @author aghost */
public class User implements Serializable {

  private static final long serialVersionUID = 1554426570644L;

  /**
   * 主键
   *
   * <p>isNullAble:0
   */
  private Integer id;

  /** isNullAble:0 */
  private String username;

  /** isNullAble:0 */
  private String passwd;

  /** isNullAble:0 */
  private String salt;

  /** isNullAble:0,defaultVal:CURRENT_TIMESTAMP */
  private Date create_time;

  /** isNullAble:1 */
  private Date update_time;

  /** isNullAble:0 */
  private String uuid;

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return this.id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public String getPasswd() {
    return this.passwd;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getSalt() {
    return this.salt;
  }

  public void setCreate_time(Date create_time) {
    this.create_time = create_time;
  }

  public Date getCreate_time() {
    return this.create_time;
  }

  public void setUpdate_time(Date update_time) {
    this.update_time = update_time;
  }

  public Date getUpdate_time() {
    return this.update_time;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getUuid() {
    return this.uuid;
  }

  @Override
  public String toString() {
    return "User{"
        + "id='"
        + id
        + '\''
        + "username='"
        + username
        + '\''
        + "passwd='"
        + passwd
        + '\''
        + "salt='"
        + salt
        + '\''
        + "create_time='"
        + create_time
        + '\''
        + "update_time='"
        + update_time
        + '\''
        + "uuid='"
        + uuid
        + '\''
        + '}';
  }

  public static Builder Build() {
    return new Builder();
  }

  public static ConditionBuilder ConditionBuild() {
    return new ConditionBuilder();
  }

  public static UpdateBuilder UpdateBuild() {
    return new UpdateBuilder();
  }

  public static QueryBuilder QueryBuild() {
    return new QueryBuilder();
  }

  public static class UpdateBuilder {

    private User set;

    private ConditionBuilder where;

    public UpdateBuilder set(User set) {
      this.set = set;
      return this;
    }

    public User getSet() {
      return this.set;
    }

    public UpdateBuilder where(ConditionBuilder where) {
      this.where = where;
      return this;
    }

    public ConditionBuilder getWhere() {
      return this.where;
    }

    public UpdateBuilder build() {
      return this;
    }
  }

  public static class QueryBuilder extends User {
    /** 需要返回的列 */
    private Map<String, Object> fetchFields;

    public Map<String, Object> getFetchFields() {
      return this.fetchFields;
    }

    private List<Integer> idList;

    public List<Integer> getIdList() {
      return this.idList;
    }

    private Integer idSt;

    private Integer idEd;

    public Integer getIdSt() {
      return this.idSt;
    }

    public Integer getIdEd() {
      return this.idEd;
    }

    private List<String> usernameList;

    public List<String> getUsernameList() {
      return this.usernameList;
    }

    private List<String> fuzzyUsername;

    public List<String> getFuzzyUsername() {
      return this.fuzzyUsername;
    }

    private List<String> rightFuzzyUsername;

    public List<String> getRightFuzzyUsername() {
      return this.rightFuzzyUsername;
    }

    private List<String> passwdList;

    public List<String> getPasswdList() {
      return this.passwdList;
    }

    private List<String> fuzzyPasswd;

    public List<String> getFuzzyPasswd() {
      return this.fuzzyPasswd;
    }

    private List<String> rightFuzzyPasswd;

    public List<String> getRightFuzzyPasswd() {
      return this.rightFuzzyPasswd;
    }

    private List<String> saltList;

    public List<String> getSaltList() {
      return this.saltList;
    }

    private List<String> fuzzySalt;

    public List<String> getFuzzySalt() {
      return this.fuzzySalt;
    }

    private List<String> rightFuzzySalt;

    public List<String> getRightFuzzySalt() {
      return this.rightFuzzySalt;
    }

    private List<Date> create_timeList;

    public List<Date> getCreate_timeList() {
      return this.create_timeList;
    }

    private Date create_timeSt;

    private Date create_timeEd;

    public Date getCreate_timeSt() {
      return this.create_timeSt;
    }

    public Date getCreate_timeEd() {
      return this.create_timeEd;
    }

    private List<Date> update_timeList;

    public List<Date> getUpdate_timeList() {
      return this.update_timeList;
    }

    private Date update_timeSt;

    private Date update_timeEd;

    public Date getUpdate_timeSt() {
      return this.update_timeSt;
    }

    public Date getUpdate_timeEd() {
      return this.update_timeEd;
    }

    private List<String> uuidList;

    public List<String> getUuidList() {
      return this.uuidList;
    }

    private List<String> fuzzyUuid;

    public List<String> getFuzzyUuid() {
      return this.fuzzyUuid;
    }

    private List<String> rightFuzzyUuid;

    public List<String> getRightFuzzyUuid() {
      return this.rightFuzzyUuid;
    }

    private QueryBuilder() {
      this.fetchFields = new HashMap<>();
    }

    public QueryBuilder idBetWeen(Integer idSt, Integer idEd) {
      this.idSt = idSt;
      this.idEd = idEd;
      return this;
    }

    public QueryBuilder idGreaterEqThan(Integer idSt) {
      this.idSt = idSt;
      return this;
    }

    public QueryBuilder idLessEqThan(Integer idEd) {
      this.idEd = idEd;
      return this;
    }

    public QueryBuilder id(Integer id) {
      setId(id);
      return this;
    }

    public QueryBuilder idList(Integer... id) {
      this.idList = solveNullList(id);
      return this;
    }

    public QueryBuilder idList(List<Integer> id) {
      this.idList = id;
      return this;
    }

    public QueryBuilder fetchId() {
      setFetchFields("fetchFields", "id");
      return this;
    }

    public QueryBuilder excludeId() {
      setFetchFields("excludeFields", "id");
      return this;
    }

    public QueryBuilder fuzzyUsername(List<String> fuzzyUsername) {
      this.fuzzyUsername = fuzzyUsername;
      return this;
    }

    public QueryBuilder fuzzyUsername(String... fuzzyUsername) {
      this.fuzzyUsername = solveNullList(fuzzyUsername);
      return this;
    }

    public QueryBuilder rightFuzzyUsername(List<String> rightFuzzyUsername) {
      this.rightFuzzyUsername = rightFuzzyUsername;
      return this;
    }

    public QueryBuilder rightFuzzyUsername(String... rightFuzzyUsername) {
      this.rightFuzzyUsername = solveNullList(rightFuzzyUsername);
      return this;
    }

    public QueryBuilder username(String username) {
      setUsername(username);
      return this;
    }

    public QueryBuilder usernameList(String... username) {
      this.usernameList = solveNullList(username);
      return this;
    }

    public QueryBuilder usernameList(List<String> username) {
      this.usernameList = username;
      return this;
    }

    public QueryBuilder fetchUsername() {
      setFetchFields("fetchFields", "username");
      return this;
    }

    public QueryBuilder excludeUsername() {
      setFetchFields("excludeFields", "username");
      return this;
    }

    public QueryBuilder fuzzyPasswd(List<String> fuzzyPasswd) {
      this.fuzzyPasswd = fuzzyPasswd;
      return this;
    }

    public QueryBuilder fuzzyPasswd(String... fuzzyPasswd) {
      this.fuzzyPasswd = solveNullList(fuzzyPasswd);
      return this;
    }

    public QueryBuilder rightFuzzyPasswd(List<String> rightFuzzyPasswd) {
      this.rightFuzzyPasswd = rightFuzzyPasswd;
      return this;
    }

    public QueryBuilder rightFuzzyPasswd(String... rightFuzzyPasswd) {
      this.rightFuzzyPasswd = solveNullList(rightFuzzyPasswd);
      return this;
    }

    public QueryBuilder passwd(String passwd) {
      setPasswd(passwd);
      return this;
    }

    public QueryBuilder passwdList(String... passwd) {
      this.passwdList = solveNullList(passwd);
      return this;
    }

    public QueryBuilder passwdList(List<String> passwd) {
      this.passwdList = passwd;
      return this;
    }

    public QueryBuilder fetchPasswd() {
      setFetchFields("fetchFields", "passwd");
      return this;
    }

    public QueryBuilder excludePasswd() {
      setFetchFields("excludeFields", "passwd");
      return this;
    }

    public QueryBuilder fuzzySalt(List<String> fuzzySalt) {
      this.fuzzySalt = fuzzySalt;
      return this;
    }

    public QueryBuilder fuzzySalt(String... fuzzySalt) {
      this.fuzzySalt = solveNullList(fuzzySalt);
      return this;
    }

    public QueryBuilder rightFuzzySalt(List<String> rightFuzzySalt) {
      this.rightFuzzySalt = rightFuzzySalt;
      return this;
    }

    public QueryBuilder rightFuzzySalt(String... rightFuzzySalt) {
      this.rightFuzzySalt = solveNullList(rightFuzzySalt);
      return this;
    }

    public QueryBuilder salt(String salt) {
      setSalt(salt);
      return this;
    }

    public QueryBuilder saltList(String... salt) {
      this.saltList = solveNullList(salt);
      return this;
    }

    public QueryBuilder saltList(List<String> salt) {
      this.saltList = salt;
      return this;
    }

    public QueryBuilder fetchSalt() {
      setFetchFields("fetchFields", "salt");
      return this;
    }

    public QueryBuilder excludeSalt() {
      setFetchFields("excludeFields", "salt");
      return this;
    }

    public QueryBuilder create_timeBetWeen(Date create_timeSt, Date create_timeEd) {
      this.create_timeSt = create_timeSt;
      this.create_timeEd = create_timeEd;
      return this;
    }

    public QueryBuilder create_timeGreaterEqThan(Date create_timeSt) {
      this.create_timeSt = create_timeSt;
      return this;
    }

    public QueryBuilder create_timeLessEqThan(Date create_timeEd) {
      this.create_timeEd = create_timeEd;
      return this;
    }

    public QueryBuilder create_time(Date create_time) {
      setCreate_time(create_time);
      return this;
    }

    public QueryBuilder create_timeList(Date... create_time) {
      this.create_timeList = solveNullList(create_time);
      return this;
    }

    public QueryBuilder create_timeList(List<Date> create_time) {
      this.create_timeList = create_time;
      return this;
    }

    public QueryBuilder fetchCreate_time() {
      setFetchFields("fetchFields", "create_time");
      return this;
    }

    public QueryBuilder excludeCreate_time() {
      setFetchFields("excludeFields", "create_time");
      return this;
    }

    public QueryBuilder update_timeBetWeen(Date update_timeSt, Date update_timeEd) {
      this.update_timeSt = update_timeSt;
      this.update_timeEd = update_timeEd;
      return this;
    }

    public QueryBuilder update_timeGreaterEqThan(Date update_timeSt) {
      this.update_timeSt = update_timeSt;
      return this;
    }

    public QueryBuilder update_timeLessEqThan(Date update_timeEd) {
      this.update_timeEd = update_timeEd;
      return this;
    }

    public QueryBuilder update_time(Date update_time) {
      setUpdate_time(update_time);
      return this;
    }

    public QueryBuilder update_timeList(Date... update_time) {
      this.update_timeList = solveNullList(update_time);
      return this;
    }

    public QueryBuilder update_timeList(List<Date> update_time) {
      this.update_timeList = update_time;
      return this;
    }

    public QueryBuilder fetchUpdate_time() {
      setFetchFields("fetchFields", "update_time");
      return this;
    }

    public QueryBuilder excludeUpdate_time() {
      setFetchFields("excludeFields", "update_time");
      return this;
    }

    public QueryBuilder fuzzyUuid(List<String> fuzzyUuid) {
      this.fuzzyUuid = fuzzyUuid;
      return this;
    }

    public QueryBuilder fuzzyUuid(String... fuzzyUuid) {
      this.fuzzyUuid = solveNullList(fuzzyUuid);
      return this;
    }

    public QueryBuilder rightFuzzyUuid(List<String> rightFuzzyUuid) {
      this.rightFuzzyUuid = rightFuzzyUuid;
      return this;
    }

    public QueryBuilder rightFuzzyUuid(String... rightFuzzyUuid) {
      this.rightFuzzyUuid = solveNullList(rightFuzzyUuid);
      return this;
    }

    public QueryBuilder uuid(String uuid) {
      setUuid(uuid);
      return this;
    }

    public QueryBuilder uuidList(String... uuid) {
      this.uuidList = solveNullList(uuid);
      return this;
    }

    public QueryBuilder uuidList(List<String> uuid) {
      this.uuidList = uuid;
      return this;
    }

    public QueryBuilder fetchUuid() {
      setFetchFields("fetchFields", "uuid");
      return this;
    }

    public QueryBuilder excludeUuid() {
      setFetchFields("excludeFields", "uuid");
      return this;
    }

    private <T> List<T> solveNullList(T... objs) {
      if (objs != null) {
        List<T> list = new ArrayList<>();
        for (T item : objs) {
          if (item != null) {
            list.add(item);
          }
        }
        return list;
      }
      return null;
    }

    public QueryBuilder fetchAll() {
      this.fetchFields.put("AllFields", true);
      return this;
    }

    public QueryBuilder addField(String... fields) {
      List<String> list = new ArrayList<>();
      if (fields != null) {
        for (String field : fields) {
          list.add(field);
        }
      }
      this.fetchFields.put("otherFields", list);
      return this;
    }

    @SuppressWarnings("unchecked")
    private void setFetchFields(String key, String val) {
      Map<String, Boolean> fields = (Map<String, Boolean>) this.fetchFields.get(key);
      if (fields == null) {
        fields = new HashMap<>();
      }
      fields.put(val, true);
      this.fetchFields.put(key, fields);
    }

    public User build() {
      return this;
    }
  }

  public static class ConditionBuilder {
    private List<Integer> idList;

    public List<Integer> getIdList() {
      return this.idList;
    }

    private Integer idSt;

    private Integer idEd;

    public Integer getIdSt() {
      return this.idSt;
    }

    public Integer getIdEd() {
      return this.idEd;
    }

    private List<String> usernameList;

    public List<String> getUsernameList() {
      return this.usernameList;
    }

    private List<String> fuzzyUsername;

    public List<String> getFuzzyUsername() {
      return this.fuzzyUsername;
    }

    private List<String> rightFuzzyUsername;

    public List<String> getRightFuzzyUsername() {
      return this.rightFuzzyUsername;
    }

    private List<String> passwdList;

    public List<String> getPasswdList() {
      return this.passwdList;
    }

    private List<String> fuzzyPasswd;

    public List<String> getFuzzyPasswd() {
      return this.fuzzyPasswd;
    }

    private List<String> rightFuzzyPasswd;

    public List<String> getRightFuzzyPasswd() {
      return this.rightFuzzyPasswd;
    }

    private List<String> saltList;

    public List<String> getSaltList() {
      return this.saltList;
    }

    private List<String> fuzzySalt;

    public List<String> getFuzzySalt() {
      return this.fuzzySalt;
    }

    private List<String> rightFuzzySalt;

    public List<String> getRightFuzzySalt() {
      return this.rightFuzzySalt;
    }

    private List<Date> create_timeList;

    public List<Date> getCreate_timeList() {
      return this.create_timeList;
    }

    private Date create_timeSt;

    private Date create_timeEd;

    public Date getCreate_timeSt() {
      return this.create_timeSt;
    }

    public Date getCreate_timeEd() {
      return this.create_timeEd;
    }

    private List<Date> update_timeList;

    public List<Date> getUpdate_timeList() {
      return this.update_timeList;
    }

    private Date update_timeSt;

    private Date update_timeEd;

    public Date getUpdate_timeSt() {
      return this.update_timeSt;
    }

    public Date getUpdate_timeEd() {
      return this.update_timeEd;
    }

    private List<String> uuidList;

    public List<String> getUuidList() {
      return this.uuidList;
    }

    private List<String> fuzzyUuid;

    public List<String> getFuzzyUuid() {
      return this.fuzzyUuid;
    }

    private List<String> rightFuzzyUuid;

    public List<String> getRightFuzzyUuid() {
      return this.rightFuzzyUuid;
    }

    public ConditionBuilder idBetWeen(Integer idSt, Integer idEd) {
      this.idSt = idSt;
      this.idEd = idEd;
      return this;
    }

    public ConditionBuilder idGreaterEqThan(Integer idSt) {
      this.idSt = idSt;
      return this;
    }

    public ConditionBuilder idLessEqThan(Integer idEd) {
      this.idEd = idEd;
      return this;
    }

    public ConditionBuilder idList(Integer... id) {
      this.idList = solveNullList(id);
      return this;
    }

    public ConditionBuilder idList(List<Integer> id) {
      this.idList = id;
      return this;
    }

    public ConditionBuilder fuzzyUsername(List<String> fuzzyUsername) {
      this.fuzzyUsername = fuzzyUsername;
      return this;
    }

    public ConditionBuilder fuzzyUsername(String... fuzzyUsername) {
      this.fuzzyUsername = solveNullList(fuzzyUsername);
      return this;
    }

    public ConditionBuilder rightFuzzyUsername(List<String> rightFuzzyUsername) {
      this.rightFuzzyUsername = rightFuzzyUsername;
      return this;
    }

    public ConditionBuilder rightFuzzyUsername(String... rightFuzzyUsername) {
      this.rightFuzzyUsername = solveNullList(rightFuzzyUsername);
      return this;
    }

    public ConditionBuilder usernameList(String... username) {
      this.usernameList = solveNullList(username);
      return this;
    }

    public ConditionBuilder usernameList(List<String> username) {
      this.usernameList = username;
      return this;
    }

    public ConditionBuilder fuzzyPasswd(List<String> fuzzyPasswd) {
      this.fuzzyPasswd = fuzzyPasswd;
      return this;
    }

    public ConditionBuilder fuzzyPasswd(String... fuzzyPasswd) {
      this.fuzzyPasswd = solveNullList(fuzzyPasswd);
      return this;
    }

    public ConditionBuilder rightFuzzyPasswd(List<String> rightFuzzyPasswd) {
      this.rightFuzzyPasswd = rightFuzzyPasswd;
      return this;
    }

    public ConditionBuilder rightFuzzyPasswd(String... rightFuzzyPasswd) {
      this.rightFuzzyPasswd = solveNullList(rightFuzzyPasswd);
      return this;
    }

    public ConditionBuilder passwdList(String... passwd) {
      this.passwdList = solveNullList(passwd);
      return this;
    }

    public ConditionBuilder passwdList(List<String> passwd) {
      this.passwdList = passwd;
      return this;
    }

    public ConditionBuilder fuzzySalt(List<String> fuzzySalt) {
      this.fuzzySalt = fuzzySalt;
      return this;
    }

    public ConditionBuilder fuzzySalt(String... fuzzySalt) {
      this.fuzzySalt = solveNullList(fuzzySalt);
      return this;
    }

    public ConditionBuilder rightFuzzySalt(List<String> rightFuzzySalt) {
      this.rightFuzzySalt = rightFuzzySalt;
      return this;
    }

    public ConditionBuilder rightFuzzySalt(String... rightFuzzySalt) {
      this.rightFuzzySalt = solveNullList(rightFuzzySalt);
      return this;
    }

    public ConditionBuilder saltList(String... salt) {
      this.saltList = solveNullList(salt);
      return this;
    }

    public ConditionBuilder saltList(List<String> salt) {
      this.saltList = salt;
      return this;
    }

    public ConditionBuilder create_timeBetWeen(Date create_timeSt, Date create_timeEd) {
      this.create_timeSt = create_timeSt;
      this.create_timeEd = create_timeEd;
      return this;
    }

    public ConditionBuilder create_timeGreaterEqThan(Date create_timeSt) {
      this.create_timeSt = create_timeSt;
      return this;
    }

    public ConditionBuilder create_timeLessEqThan(Date create_timeEd) {
      this.create_timeEd = create_timeEd;
      return this;
    }

    public ConditionBuilder create_timeList(Date... create_time) {
      this.create_timeList = solveNullList(create_time);
      return this;
    }

    public ConditionBuilder create_timeList(List<Date> create_time) {
      this.create_timeList = create_time;
      return this;
    }

    public ConditionBuilder update_timeBetWeen(Date update_timeSt, Date update_timeEd) {
      this.update_timeSt = update_timeSt;
      this.update_timeEd = update_timeEd;
      return this;
    }

    public ConditionBuilder update_timeGreaterEqThan(Date update_timeSt) {
      this.update_timeSt = update_timeSt;
      return this;
    }

    public ConditionBuilder update_timeLessEqThan(Date update_timeEd) {
      this.update_timeEd = update_timeEd;
      return this;
    }

    public ConditionBuilder update_timeList(Date... update_time) {
      this.update_timeList = solveNullList(update_time);
      return this;
    }

    public ConditionBuilder update_timeList(List<Date> update_time) {
      this.update_timeList = update_time;
      return this;
    }

    public ConditionBuilder fuzzyUuid(List<String> fuzzyUuid) {
      this.fuzzyUuid = fuzzyUuid;
      return this;
    }

    public ConditionBuilder fuzzyUuid(String... fuzzyUuid) {
      this.fuzzyUuid = solveNullList(fuzzyUuid);
      return this;
    }

    public ConditionBuilder rightFuzzyUuid(List<String> rightFuzzyUuid) {
      this.rightFuzzyUuid = rightFuzzyUuid;
      return this;
    }

    public ConditionBuilder rightFuzzyUuid(String... rightFuzzyUuid) {
      this.rightFuzzyUuid = solveNullList(rightFuzzyUuid);
      return this;
    }

    public ConditionBuilder uuidList(String... uuid) {
      this.uuidList = solveNullList(uuid);
      return this;
    }

    public ConditionBuilder uuidList(List<String> uuid) {
      this.uuidList = uuid;
      return this;
    }

    private <T> List<T> solveNullList(T... objs) {
      if (objs != null) {
        List<T> list = new ArrayList<>();
        for (T item : objs) {
          if (item != null) {
            list.add(item);
          }
        }
        return list;
      }
      return null;
    }

    public ConditionBuilder build() {
      return this;
    }
  }

  public static class Builder {

    private User obj;

    public Builder() {
      this.obj = new User();
    }

    public Builder id(Integer id) {
      this.obj.setId(id);
      return this;
    }

    public Builder username(String username) {
      this.obj.setUsername(username);
      return this;
    }

    public Builder passwd(String passwd) {
      this.obj.setPasswd(passwd);
      return this;
    }

    public Builder salt(String salt) {
      this.obj.setSalt(salt);
      return this;
    }

    public Builder create_time(Date create_time) {
      this.obj.setCreate_time(create_time);
      return this;
    }

    public Builder update_time(Date update_time) {
      this.obj.setUpdate_time(update_time);
      return this;
    }

    public Builder uuid(String uuid) {
      this.obj.setUuid(uuid);
      return this;
    }

    public User build() {
      return obj;
    }
  }
}
