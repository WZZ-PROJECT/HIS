package com.neu.his.cloud.zuul.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class WxResults implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "病人Id")
    private Long patientId;

    @ApiModelProperty(value = "微信支付订单号 ")
    private String transactionId;

    @ApiModelProperty(value = "用户标识")
    private String openid;

    @ApiModelProperty(value = "商户号")
    private String mchId;

    @ApiModelProperty(value = "商户订单号 商户系统内部订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "订单金额")
    private String totalFee;

    @ApiModelProperty(value = "支付完成时间")
    private Date timeEnd;

    private Long type;

    @ApiModelProperty(value = "状态  0：未退款  1：已退款")
    private Long state;

    @ApiModelProperty(value = "结果")
    private String results;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", patientId=").append(patientId);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", openid=").append(openid);
        sb.append(", mchId=").append(mchId);
        sb.append(", outTradeNo=").append(outTradeNo);
        sb.append(", totalFee=").append(totalFee);
        sb.append(", timeEnd=").append(timeEnd);
        sb.append(", type=").append(type);
        sb.append(", state=").append(state);
        sb.append(", results=").append(results);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
