package com.bjpowernode.crm.workbench.pojo;

public class ClueActivityRelation {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_clue_activity_relation.id
     *
     * @mbggenerated Sun Jul 14 12:21:00 CST 2024
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_clue_activity_relation.clue_id
     *
     * @mbggenerated Sun Jul 14 12:21:00 CST 2024
     */
    private String clueId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_clue_activity_relation.activity_id
     *
     * @mbggenerated Sun Jul 14 12:21:00 CST 2024
     */
    private String activityId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_clue_activity_relation.id
     *
     * @return the value of tbl_clue_activity_relation.id
     *
     * @mbggenerated Sun Jul 14 12:21:00 CST 2024
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_clue_activity_relation.id
     *
     * @param id the value for tbl_clue_activity_relation.id
     *
     * @mbggenerated Sun Jul 14 12:21:00 CST 2024
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_clue_activity_relation.clue_id
     *
     * @return the value of tbl_clue_activity_relation.clue_id
     *
     * @mbggenerated Sun Jul 14 12:21:00 CST 2024
     */
    public String getClueId() {
        return clueId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_clue_activity_relation.clue_id
     *
     * @param clueId the value for tbl_clue_activity_relation.clue_id
     *
     * @mbggenerated Sun Jul 14 12:21:00 CST 2024
     */
    public void setClueId(String clueId) {
        this.clueId = clueId == null ? null : clueId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_clue_activity_relation.activity_id
     *
     * @return the value of tbl_clue_activity_relation.activity_id
     *
     * @mbggenerated Sun Jul 14 12:21:00 CST 2024
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_clue_activity_relation.activity_id
     *
     * @param activityId the value for tbl_clue_activity_relation.activity_id
     *
     * @mbggenerated Sun Jul 14 12:21:00 CST 2024
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }
}