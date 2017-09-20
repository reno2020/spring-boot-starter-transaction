package org.throwable.ons.orm.entity;

import org.throwable.mapper.core.common.annotation.Column;
import org.throwable.mapper.core.common.annotation.Table;

import java.time.LocalDateTime;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 12:13
 */
@Table(value = "t_transaction_log")
public class TransactionLog extends BaseEntity{

    @Column(value = "transaction_id")
    private Long transactionId;
    @Column(value = "transaction_message_id")
    private Long transactionMessageId;

    @Column(value = "message_id")
    private String messageId;
    @Column(value = "unique_code")
    private String uniqueCode;

    @Column(value = "transaction_status")
    private String transactionStatus;
    @Column(value = "fire_transaction_status")
    private String fireTransactionStatus;
    @Column(value = "push_message_status")
    private String pushMessageStatus;

    @Column(value = "fire_attempt_times")
    private Byte fireAttemptTimes;
    @Column(value = "check_attempt_times")
    private Byte checkAttemptTimes;
    @Column(value = "push_attempt_times")
    private Byte pushAttemptTimes;

    @Column(value = "fire_transaction_time")
    private LocalDateTime fireTransactionTime;
    @Column(value = "transaction_end_time")
    private LocalDateTime transactionEndTime;
    @Column(value = "push_message_time")
    private LocalDateTime pushMessageTime;

    @Column(value = "checker_class_name")
    private String checkerClassName;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionMessageId() {
        return transactionMessageId;
    }

    public void setTransactionMessageId(Long transactionMessageId) {
        this.transactionMessageId = transactionMessageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getFireTransactionStatus() {
        return fireTransactionStatus;
    }

    public void setFireTransactionStatus(String fireTransactionStatus) {
        this.fireTransactionStatus = fireTransactionStatus;
    }

    public String getPushMessageStatus() {
        return pushMessageStatus;
    }

    public void setPushMessageStatus(String pushMessageStatus) {
        this.pushMessageStatus = pushMessageStatus;
    }

    public Byte getFireAttemptTimes() {
        return fireAttemptTimes;
    }

    public void setFireAttemptTimes(Byte fireAttemptTimes) {
        this.fireAttemptTimes = fireAttemptTimes;
    }

    public Byte getCheckAttemptTimes() {
        return checkAttemptTimes;
    }

    public void setCheckAttemptTimes(Byte checkAttemptTimes) {
        this.checkAttemptTimes = checkAttemptTimes;
    }

    public Byte getPushAttemptTimes() {
        return pushAttemptTimes;
    }

    public void setPushAttemptTimes(Byte pushAttemptTimes) {
        this.pushAttemptTimes = pushAttemptTimes;
    }

    public LocalDateTime getFireTransactionTime() {
        return fireTransactionTime;
    }

    public void setFireTransactionTime(LocalDateTime fireTransactionTime) {
        this.fireTransactionTime = fireTransactionTime;
    }

    public LocalDateTime getTransactionEndTime() {
        return transactionEndTime;
    }

    public void setTransactionEndTime(LocalDateTime transactionEndTime) {
        this.transactionEndTime = transactionEndTime;
    }

    public LocalDateTime getPushMessageTime() {
        return pushMessageTime;
    }

    public void setPushMessageTime(LocalDateTime pushMessageTime) {
        this.pushMessageTime = pushMessageTime;
    }

    public String getCheckerClassName() {
        return checkerClassName;
    }

    public void setCheckerClassName(String checkerClassName) {
        this.checkerClassName = checkerClassName;
    }
}
