package com.misakamikoto.schedule.mapper;

import com.misakamikoto.schedule.model.ScheduleVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * The interface Schedule mapper.
 */
@Repository
public interface ScheduleMapper {
    /**
     * Gets schedule.
     *
     * @param userId the user id
     * @return the schedule
     */
    List<ScheduleVO> getSchedule(String userId);

    /**
     * Save schedule.
     *
     * @param timeTableMap the time table map
     */
    void saveSchedule(Map timeTableMap);

    /**
     * Clear schedule.
     *
     * @param userId the user id
     */
    void clearSchedule(String userId);
}
