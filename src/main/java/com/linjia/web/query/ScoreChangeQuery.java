package com.linjia.web.query;

import java.util.Date;

import com.linjia.base.query.Query;

public class ScoreChangeQuery extends Query{
	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_score_change.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_score_change.user_id
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_score_change.score
     *
     * @mbggenerated
     */
    private Integer score;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_score_change.type
     *
     * @mbggenerated
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_score_change.cre_date
     *
     * @mbggenerated
     */
    private Date creDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_score_change.deleted
     *
     * @mbggenerated
     */
    private Boolean deleted;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_score_change.id
     *
     * @return the value of b_score_change.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_score_change.id
     *
     * @param id the value for b_score_change.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_score_change.user_id
     *
     * @return the value of b_score_change.user_id
     *
     * @mbggenerated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_score_change.user_id
     *
     * @param userId the value for b_score_change.user_id
     *
     * @mbggenerated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_score_change.score
     *
     * @return the value of b_score_change.score
     *
     * @mbggenerated
     */
    public Integer getScore() {
        return score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_score_change.score
     *
     * @param score the value for b_score_change.score
     *
     * @mbggenerated
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_score_change.type
     *
     * @return the value of b_score_change.type
     *
     * @mbggenerated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_score_change.type
     *
     * @param type the value for b_score_change.type
     *
     * @mbggenerated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_score_change.cre_date
     *
     * @return the value of b_score_change.cre_date
     *
     * @mbggenerated
     */
    public Date getCreDate() {
        return creDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_score_change.cre_date
     *
     * @param creDate the value for b_score_change.cre_date
     *
     * @mbggenerated
     */
    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_score_change.deleted
     *
     * @return the value of b_score_change.deleted
     *
     * @mbggenerated
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_score_change.deleted
     *
     * @param deleted the value for b_score_change.deleted
     *
     * @mbggenerated
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}