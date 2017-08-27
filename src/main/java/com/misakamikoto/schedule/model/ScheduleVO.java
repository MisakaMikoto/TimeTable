package com.misakamikoto.schedule.model;

import java.io.Serializable;

/**
 * The type Schedule vo.
 */
public class ScheduleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The User id.
     */
    String userId;
    /**
     * The Time.
     */
    int time;
    /**
     * The Subject.
     */
    String subject;

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject.
     *
     * @param subject the subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
