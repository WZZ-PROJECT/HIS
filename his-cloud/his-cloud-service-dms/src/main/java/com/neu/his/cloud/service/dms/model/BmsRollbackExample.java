package com.neu.his.cloud.service.dms.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BmsRollbackExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BmsRollbackExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAccountCodeIsNull() {
            addCriterion("account_code is null");
            return (Criteria) this;
        }

        public Criteria andAccountCodeIsNotNull() {
            addCriterion("account_code is not null");
            return (Criteria) this;
        }

        public Criteria andAccountCodeEqualTo(String value) {
            addCriterion("account_code =", value, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeNotEqualTo(String value) {
            addCriterion("account_code <>", value, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeGreaterThan(String value) {
            addCriterion("account_code >", value, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeGreaterThanOrEqualTo(String value) {
            addCriterion("account_code >=", value, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeLessThan(String value) {
            addCriterion("account_code <", value, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeLessThanOrEqualTo(String value) {
            addCriterion("account_code <=", value, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeLike(String value) {
            addCriterion("account_code like", value, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeNotLike(String value) {
            addCriterion("account_code not like", value, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeIn(List<String> values) {
            addCriterion("account_code in", values, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeNotIn(List<String> values) {
            addCriterion("account_code not in", values, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeBetween(String value1, String value2) {
            addCriterion("account_code between", value1, value2, "accountCode");
            return (Criteria) this;
        }

        public Criteria andAccountCodeNotBetween(String value1, String value2) {
            addCriterion("account_code not between", value1, value2, "accountCode");
            return (Criteria) this;
        }

        public Criteria andRbAmountIsNull() {
            addCriterion("rb_amount is null");
            return (Criteria) this;
        }

        public Criteria andRbAmountIsNotNull() {
            addCriterion("rb_amount is not null");
            return (Criteria) this;
        }

        public Criteria andRbAmountEqualTo(BigDecimal value) {
            addCriterion("rb_amount =", value, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbAmountNotEqualTo(BigDecimal value) {
            addCriterion("rb_amount <>", value, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbAmountGreaterThan(BigDecimal value) {
            addCriterion("rb_amount >", value, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("rb_amount >=", value, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbAmountLessThan(BigDecimal value) {
            addCriterion("rb_amount <", value, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("rb_amount <=", value, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbAmountIn(List<BigDecimal> values) {
            addCriterion("rb_amount in", values, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbAmountNotIn(List<BigDecimal> values) {
            addCriterion("rb_amount not in", values, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rb_amount between", value1, value2, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rb_amount not between", value1, value2, "rbAmount");
            return (Criteria) this;
        }

        public Criteria andRbStateIsNull() {
            addCriterion("rb_state is null");
            return (Criteria) this;
        }

        public Criteria andRbStateIsNotNull() {
            addCriterion("rb_state is not null");
            return (Criteria) this;
        }

        public Criteria andRbStateEqualTo(Integer value) {
            addCriterion("rb_state =", value, "rbState");
            return (Criteria) this;
        }

        public Criteria andRbStateNotEqualTo(Integer value) {
            addCriterion("rb_state <>", value, "rbState");
            return (Criteria) this;
        }

        public Criteria andRbStateGreaterThan(Integer value) {
            addCriterion("rb_state >", value, "rbState");
            return (Criteria) this;
        }

        public Criteria andRbStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("rb_state >=", value, "rbState");
            return (Criteria) this;
        }

        public Criteria andRbStateLessThan(Integer value) {
            addCriterion("rb_state <", value, "rbState");
            return (Criteria) this;
        }

        public Criteria andRbStateLessThanOrEqualTo(Integer value) {
            addCriterion("rb_state <=", value, "rbState");
            return (Criteria) this;
        }

        public Criteria andRbStateIn(List<Integer> values) {
            addCriterion("rb_state in", values, "rbState");
            return (Criteria) this;
        }

        public Criteria andRbStateNotIn(List<Integer> values) {
            addCriterion("rb_state not in", values, "rbState");
            return (Criteria) this;
        }

        public Criteria andRbStateBetween(Integer value1, Integer value2) {
            addCriterion("rb_state between", value1, value2, "rbState");
            return (Criteria) this;
        }

        public Criteria andRbStateNotBetween(Integer value1, Integer value2) {
            addCriterion("rb_state not between", value1, value2, "rbState");
            return (Criteria) this;
        }

        public Criteria andAppTimeIsNull() {
            addCriterion("app_time is null");
            return (Criteria) this;
        }

        public Criteria andAppTimeIsNotNull() {
            addCriterion("app_time is not null");
            return (Criteria) this;
        }

        public Criteria andAppTimeEqualTo(Date value) {
            addCriterion("app_time =", value, "appTime");
            return (Criteria) this;
        }

        public Criteria andAppTimeNotEqualTo(Date value) {
            addCriterion("app_time <>", value, "appTime");
            return (Criteria) this;
        }

        public Criteria andAppTimeGreaterThan(Date value) {
            addCriterion("app_time >", value, "appTime");
            return (Criteria) this;
        }

        public Criteria andAppTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("app_time >=", value, "appTime");
            return (Criteria) this;
        }

        public Criteria andAppTimeLessThan(Date value) {
            addCriterion("app_time <", value, "appTime");
            return (Criteria) this;
        }

        public Criteria andAppTimeLessThanOrEqualTo(Date value) {
            addCriterion("app_time <=", value, "appTime");
            return (Criteria) this;
        }

        public Criteria andAppTimeIn(List<Date> values) {
            addCriterion("app_time in", values, "appTime");
            return (Criteria) this;
        }

        public Criteria andAppTimeNotIn(List<Date> values) {
            addCriterion("app_time not in", values, "appTime");
            return (Criteria) this;
        }

        public Criteria andAppTimeBetween(Date value1, Date value2) {
            addCriterion("app_time between", value1, value2, "appTime");
            return (Criteria) this;
        }

        public Criteria andAppTimeNotBetween(Date value1, Date value2) {
            addCriterion("app_time not between", value1, value2, "appTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeIsNull() {
            addCriterion("rb_time is null");
            return (Criteria) this;
        }

        public Criteria andRbTimeIsNotNull() {
            addCriterion("rb_time is not null");
            return (Criteria) this;
        }

        public Criteria andRbTimeEqualTo(Date value) {
            addCriterion("rb_time =", value, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeNotEqualTo(Date value) {
            addCriterion("rb_time <>", value, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeGreaterThan(Date value) {
            addCriterion("rb_time >", value, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("rb_time >=", value, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeLessThan(Date value) {
            addCriterion("rb_time <", value, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeLessThanOrEqualTo(Date value) {
            addCriterion("rb_time <=", value, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeIn(List<Date> values) {
            addCriterion("rb_time in", values, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeNotIn(List<Date> values) {
            addCriterion("rb_time not in", values, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeBetween(Date value1, Date value2) {
            addCriterion("rb_time between", value1, value2, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTimeNotBetween(Date value1, Date value2) {
            addCriterion("rb_time not between", value1, value2, "rbTime");
            return (Criteria) this;
        }

        public Criteria andRbTypeIsNull() {
            addCriterion("rb_type is null");
            return (Criteria) this;
        }

        public Criteria andRbTypeIsNotNull() {
            addCriterion("rb_type is not null");
            return (Criteria) this;
        }

        public Criteria andRbTypeEqualTo(Integer value) {
            addCriterion("rb_type =", value, "rbType");
            return (Criteria) this;
        }

        public Criteria andRbTypeNotEqualTo(Integer value) {
            addCriterion("rb_type <>", value, "rbType");
            return (Criteria) this;
        }

        public Criteria andRbTypeGreaterThan(Integer value) {
            addCriterion("rb_type >", value, "rbType");
            return (Criteria) this;
        }

        public Criteria andRbTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("rb_type >=", value, "rbType");
            return (Criteria) this;
        }

        public Criteria andRbTypeLessThan(Integer value) {
            addCriterion("rb_type <", value, "rbType");
            return (Criteria) this;
        }

        public Criteria andRbTypeLessThanOrEqualTo(Integer value) {
            addCriterion("rb_type <=", value, "rbType");
            return (Criteria) this;
        }

        public Criteria andRbTypeIn(List<Integer> values) {
            addCriterion("rb_type in", values, "rbType");
            return (Criteria) this;
        }

        public Criteria andRbTypeNotIn(List<Integer> values) {
            addCriterion("rb_type not in", values, "rbType");
            return (Criteria) this;
        }

        public Criteria andRbTypeBetween(Integer value1, Integer value2) {
            addCriterion("rb_type between", value1, value2, "rbType");
            return (Criteria) this;
        }

        public Criteria andRbTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("rb_type not between", value1, value2, "rbType");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdIsNull() {
            addCriterion("out_trade_id is null");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdIsNotNull() {
            addCriterion("out_trade_id is not null");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdEqualTo(String value) {
            addCriterion("out_trade_id =", value, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdNotEqualTo(String value) {
            addCriterion("out_trade_id <>", value, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdGreaterThan(String value) {
            addCriterion("out_trade_id >", value, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdGreaterThanOrEqualTo(String value) {
            addCriterion("out_trade_id >=", value, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdLessThan(String value) {
            addCriterion("out_trade_id <", value, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdLessThanOrEqualTo(String value) {
            addCriterion("out_trade_id <=", value, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdLike(String value) {
            addCriterion("out_trade_id like", value, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdNotLike(String value) {
            addCriterion("out_trade_id not like", value, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdIn(List<String> values) {
            addCriterion("out_trade_id in", values, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdNotIn(List<String> values) {
            addCriterion("out_trade_id not in", values, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdBetween(String value1, String value2) {
            addCriterion("out_trade_id between", value1, value2, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andOutTradeIdNotBetween(String value1, String value2) {
            addCriterion("out_trade_id not between", value1, value2, "outTradeId");
            return (Criteria) this;
        }

        public Criteria andRosultIsNull() {
            addCriterion("rosult is null");
            return (Criteria) this;
        }

        public Criteria andRosultIsNotNull() {
            addCriterion("rosult is not null");
            return (Criteria) this;
        }

        public Criteria andRosultEqualTo(String value) {
            addCriterion("rosult =", value, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultNotEqualTo(String value) {
            addCriterion("rosult <>", value, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultGreaterThan(String value) {
            addCriterion("rosult >", value, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultGreaterThanOrEqualTo(String value) {
            addCriterion("rosult >=", value, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultLessThan(String value) {
            addCriterion("rosult <", value, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultLessThanOrEqualTo(String value) {
            addCriterion("rosult <=", value, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultLike(String value) {
            addCriterion("rosult like", value, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultNotLike(String value) {
            addCriterion("rosult not like", value, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultIn(List<String> values) {
            addCriterion("rosult in", values, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultNotIn(List<String> values) {
            addCriterion("rosult not in", values, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultBetween(String value1, String value2) {
            addCriterion("rosult between", value1, value2, "rosult");
            return (Criteria) this;
        }

        public Criteria andRosultNotBetween(String value1, String value2) {
            addCriterion("rosult not between", value1, value2, "rosult");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(Long value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(Long value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(Long value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(Long value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(Long value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(Long value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<Long> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<Long> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(Long value1, Long value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(Long value1, Long value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNull() {
            addCriterion("update_user is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNotNull() {
            addCriterion("update_user is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserEqualTo(Long value) {
            addCriterion("update_user =", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotEqualTo(Long value) {
            addCriterion("update_user <>", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThan(Long value) {
            addCriterion("update_user >", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThanOrEqualTo(Long value) {
            addCriterion("update_user >=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThan(Long value) {
            addCriterion("update_user <", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThanOrEqualTo(Long value) {
            addCriterion("update_user <=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIn(List<Long> values) {
            addCriterion("update_user in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotIn(List<Long> values) {
            addCriterion("update_user not in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserBetween(Long value1, Long value2) {
            addCriterion("update_user between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotBetween(Long value1, Long value2) {
            addCriterion("update_user not between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedEqualTo(Integer value) {
            addCriterion("is_deleted =", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotEqualTo(Integer value) {
            addCriterion("is_deleted <>", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThan(Integer value) {
            addCriterion("is_deleted >", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_deleted >=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThan(Integer value) {
            addCriterion("is_deleted <", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(Integer value) {
            addCriterion("is_deleted <=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIn(List<Integer> values) {
            addCriterion("is_deleted in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotIn(List<Integer> values) {
            addCriterion("is_deleted not in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedBetween(Integer value1, Integer value2) {
            addCriterion("is_deleted between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotBetween(Integer value1, Integer value2) {
            addCriterion("is_deleted not between", value1, value2, "isDeleted");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}