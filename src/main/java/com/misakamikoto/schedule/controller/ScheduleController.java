package com.misakamikoto.schedule.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.misakamikoto.schedule.model.ScheduleVO;
import com.misakamikoto.schedule.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * The type Schedule controller.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    /**
     * The Schedule service.
     */
    @Autowired
    ScheduleService scheduleService;

    /**
     * Gets schedule.
     *
     * @param userId the user id
     * @return the schedule
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public String getSchedule(@RequestParam("userId") String userId) {
        logger.info("method : getSchedule, param(userId: " + userId + ")");
        String[][] scheduleArray = new String[4][5];

        List<ScheduleVO> scheduleVOList = scheduleService.getSchedule(userId);
        if(scheduleVOList.size() > 0) {
            for (int i = 0; i < scheduleVOList.size(); i++) {
                String[] split = scheduleVOList.get(i).getSubject().split(",");

                for (int j = 0; j < split.length; j++) {
                    scheduleArray[i][j] = split[j];
                }
            }

            Gson gson = new GsonBuilder().create();
            String scheduleArrayString = gson.toJson(scheduleArray);

            return scheduleArrayString;

        } else {
            return "";
        }
    }

    /**
     * Create time table string.
     *
     * @param subjects the subjects
     * @return the string
     */
    @RequestMapping(value = "/createTimeTable", method = RequestMethod.POST)
    public String createTimeTable(@RequestBody String subjects) {
        logger.info("method : createTimeTable, param(subjects: " + subjects + ")");

        Gson gson = new GsonBuilder().create();
        Map timeTableJSONObject = (Map) gson.fromJson(subjects, Object.class);

        String[][] scheduleArray = scheduleService.algorithmWithTimeTable(timeTableJSONObject);
        String scheduleArrayString = gson.toJson(scheduleArray);

        return scheduleArrayString;
    }

    /**
     * Save schedule boolean.
     *
     * @param subjects the subjects
     * @return the boolean
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public boolean saveSchedule(@RequestBody String subjects) {
        logger.info("method : saveSchedule, param(subjects: " + subjects + ")");
        boolean isSave = false;

        try {
            Gson gson = new GsonBuilder().create();
            Map timeTableJSONObject = (Map) gson.fromJson(subjects, Object.class);

            scheduleService.saveSchedule(timeTableJSONObject);
            isSave = true;

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return isSave;
    }

    /**
     * Clear schedule boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public boolean clearSchedule(@RequestParam("userId") String userId) {
        logger.info("method : clearSchedule, param(userId: " + userId + ")");
        boolean isClear = false;

        try {
            scheduleService.clearSchedule(userId);
            isClear = true;

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return isClear;
    }
}
