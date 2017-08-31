package com.misakamikoto.schedule.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.misakamikoto.schedule.mapper.ScheduleMapper;
import com.misakamikoto.schedule.model.ScheduleVO;
import com.misakamikoto.schedule.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Schedule service.
 */
@Service
public class ScheduleService {
    /**
     * The Schedule mapper.
     */
    @Autowired
    ScheduleMapper scheduleMapper;
    /**
     * The constant isOK.
     */
    public static boolean isOK = false;

    /**
     * Gets schedule.
     *
     * @param userId the user id
     * @return the schedule
     */
    public List<ScheduleVO> getSchedule(String userId) {
        return scheduleMapper.getSchedule(userId);
    }

    /**
     * Save schedule.
     *
     * @param timeTableJSONObject the time table json object
     */
    public void saveSchedule(Map timeTableJSONObject) {
        scheduleMapper.saveSchedule(timeTableJSONObject);
    }

    /**
     * Clear schedule.
     *
     * @param userId the user id
     */
    public void clearSchedule(String userId) {
        scheduleMapper.clearSchedule(userId);
    }

    /**
     * Algorithm with time table string [ ] [ ].
     *
     * @param timeTableJSONObject the time table json object
     * @return the string [ ] [ ]
     */
    public String[][] algorithmWithTimeTable(Map timeTableJSONObject) {
        // init isOK
        isOK = false;

        Gson gson = new GsonBuilder().create();
        Map<String, Integer> subjectCreditMap = new HashMap<String, Integer>();

        List list = (List) timeTableJSONObject.get("subjects");
        for (int i = 0; i < list.size(); i++) {
            Subject subject = gson.fromJson(list.get(i).toString(), Subject.class);
            Double credit = Double.parseDouble(subject.getCredit());
            subjectCreditMap.put(subject.getName(), credit.intValue());
        }

        // start
        // 과목 정보 맵
        Map<String, List<String>> scheduleMap = new HashMap<String, List<String>>();
        String[][] scheduleArray = new String[4][5];

        int count = 0;
        while (!isOK) {
            ++count;
            createDummyTimeTable(scheduleMap, subjectCreditMap);
            scheduleArray = mappingDayTimeRandomList(scheduleMap);

            // 아래 세개는 원활한 데이터 만들기에 필요한 조건들이므로 순서대로 실행되어야 한다.
            // 속도를 위한 중요도는 duplicateMinimizationTimeTable > minimizationTimeTable > mappingTimeTable 순이기 때문에
            // 순서대로 진행해야 한다.
            if(duplicateMinimizationTimeTable(scheduleArray, subjectCreditMap)) {
                if(minimizationTimeTable(scheduleArray)) {
                    if(mappingTimeTable(scheduleArray, subjectCreditMap)) {
                        // 최종적으로 중복된 수업들을 제거하며 결과를 만든다.
                        createTimeTable(scheduleArray, subjectCreditMap);
                    }
                }
            }
        }

        System.out.println(count + "번의 재도전");
        System.out.println();

        for (String key : scheduleMap.keySet()) {
            System.out.println(key + "과목(" + subjectCreditMap.get(key) + "학점) : " + scheduleMap.get(key));
        }

        System.out.println();

        for (int i = 0; i < 4; i++) {
            System.out.print(i + 1 + " 교시 : ");

            for (int j = 0; j < 5; j++) {
                System.out.print(scheduleArray[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("배치가 완료된 시간표");
        for (int i = 0; i < 4; i++) {
            System.out.print(i + 1 + " 교시 : ");

            for (int j = 0; j < 5; j++) {
                System.out.print(scheduleArray[i][j] + " ");
            }
            System.out.println();
        }
        return scheduleArray;
    }

    /**
     * Create dummy time table.
     *
     * @param scheduleMap      the schedule map
     * @param subjectCreditMap the subject credit map
     */
    public void createDummyTimeTable(Map<String, List<String>> scheduleMap, Map<String, Integer> subjectCreditMap) {
        // 과목의 갯수만큼...
        for (String key : subjectCreditMap.keySet()) {
            List<Integer> dayRandomList = new ArrayList<Integer>();
            List<String> dayTimeRandomList = new ArrayList<String>();

            // 랜덤 요일 생성
            for (int i = 0; i < 6; i++) {
                createDayRandomList(dayRandomList);
            }

            // 요일에 대한 랜덤 수업 시간 생성
            for (int i = 0; i < dayRandomList.size(); i++) {
                createDayTimeRandomList(dayRandomList.get(i), dayTimeRandomList);
            }

            // 요일에 대한 랜덤 수업 시간 매핑
            scheduleMap.put(key, dayTimeRandomList);
        }
    }

    /**
     * Create day random list.
     *
     * @param dayRandomList the day random list
     */
// 랜덤 요일을 생성한다.
    // 0 : 월
    // 1 : 화
    // 2 : 수
    // 3 : 목
    // 4 : 금
    public void createDayRandomList(List<Integer> dayRandomList) {
        // 한 과목당 3일의 수업 시간을 적용한다.
        while (dayRandomList.size() < 3) {

            // 요일은 총 5일 (월 ~ 금)
            int random = (int) (Math.random() * 5);

            if (dayRandomList.size() == 0) {
                dayRandomList.add(random);

                // 요일이 겹칠 수는 없다. 때문에 중복 검사.
            } else {
                boolean checked = false;
                for (Integer val : dayRandomList) {
                    if (val == random) {
                        checked = true;
                        break;
                    }
                }
                if (!checked) {
                    dayRandomList.add(random);
                }
            }
        }
    }

    /**
     * Create day time random list.
     *
     * @param day               the day
     * @param dayTimeRandomList the day time random list
     */
// 요일에 기반하여 랜덤 수업 교시를 생성한다.
    public void createDayTimeRandomList(int day, List<String> dayTimeRandomList) {
        int random = (int) (Math.random() * 4);
        String dayTime = day + "," + random;
        dayTimeRandomList.add(dayTime);

    }

    /**
     * Mapping day time random list string [ ] [ ].
     *
     * @param scheduleMap the schedule map
     * @return the string [ ] [ ]
     */
// 수업 일정을 배치한다.
    public String[][] mappingDayTimeRandomList(Map<String, List<String>> scheduleMap) {
        // 시간표 배열
        String[][] scheduleArray = new String[4][5];

        // 2차원 배열 초기화. 빈 자리는 x로 표기.
        for (int i = 0; i < scheduleArray.length; i++) {
            for (int j = 0; j < scheduleArray[i].length; j++) {
                scheduleArray[i][j] = "x";
            }
        }

        for (String key : scheduleMap.keySet()) {
            List<String> dayTimeRandomList = scheduleMap.get(key);

            for (String val : dayTimeRandomList) {
                String[] split = val.split(",");

                // 요일
                int x = Integer.parseInt(split[0]);
                // 시간
                int y = Integer.parseInt(split[1]);

                // 세로열이 y 이므로 시간, 가로열이 x 이므로 요일
                // 빈자리 (x) 이면 해당 과목을 배치하고, 비어있지 않으면 + 해서 붙여넣는다.
                if ("x".equals(scheduleArray[y][x])) {
                    scheduleArray[y][x] = String.valueOf(key);
                } else {
                    scheduleArray[y][x] += "," + String.valueOf(key);
                }
            }
        }
        return scheduleArray;
    }

    /**
     * Duplicate minimization time table boolean.
     *
     * @param scheduleArray    the schedule array
     * @param subjectCreditMap the subject credit map
     * @return the boolean
     */
// 수업 교시에 겹치는 과목의 중복을 최소화 한다.
    // 한 가지 과목이 모두 겹치는 부분에 들어가서는 안된다. (3학점이면 3개, 2학점이면 2개가 겹치면 안된다.)
    public static boolean duplicateMinimizationTimeTable(String[][] scheduleArray, Map<String, Integer> subjectCreditMap) {
        boolean isComplete = false;

        String check = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                // 중복된 교시가 있는 경우를 저장한다.
                if(scheduleArray[i][j].length() > 1) {
                    check += scheduleArray[i][j];
                }
            }
        }

        for(String key : subjectCreditMap.keySet()) {
            // 정규식을 통해 반복된 횟수를 찾아내고
            // 학점을 통해 해당 중복 여부를 판단한다.
            Pattern p = Pattern.compile(key);
            Matcher m = p.matcher(check);

            int count = 0;
            for(int i = 0; m.find(i); i = m.end()) {
                ++count;
            }

            if(subjectCreditMap.get(key) > count) {
                isComplete = true;

            } else {
                isComplete = false;
                break;
            }
        }
        return isComplete;
    }

    /**
     * Minimization time table boolean.
     *
     * @param scheduleArray the schedule array
     * @return the boolean
     */
// 랜덤하게 생긴 시간표의 중복 최소화
    // 1. 모두 2학점이면 남는 교시는 12교시
    // 2. 모두 3학점이면 남는 교시는 4교시
    // 3. 때문에 전체 20 교시 중 16교시는 반드시 확보하여야 한다.
    //  3-1. 남는 자리 (x) 는 절대 5개가 되면 안되기 때문에 (15 교시는 3x5 로 계산해도 15학점이라 최소 교시수에 만족시키지 못한다.)
    //  3-2. 해당 교시에 과목수가 3개가 겹쳐도 안되기 때문에 (3개씩 겹치면 경우의 수가 너무 늘어나게 된다. 연산 제어의 목적)
    public boolean minimizationTimeTable(String[][] scheduleArray) {
        int xCount = 0;
        boolean isTriple = false;
        boolean isComplete = false;

        // 3-1, 3-2 검사를 수행한다.
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                // 3-3 검사를 위해 중복된 것들을 저장해 놓는다.


                String target = scheduleArray[i][j];
                String[] split = target.split(",");

                // 과목수가 3개 이상 겹치면 break
                if (split.length > 2) {
                    isTriple = true;
                    break;

                } else {
                    // 공강의 자리를 찾는다.
                    if ("x".equals(target)) {
                        ++xCount;
                    }
                }
            }

            // 과목수가 3개 이상 겹치는 이상 2중 포문도 의미가 없음 break
            if (isTriple) {
                break;
            }
        }

        if (!isTriple) {
            // 과목수가 3개이상 겹치지 않으며 공강의 자리가 5개 이하인 경우에만
            if (xCount < 4) {
                isComplete = true;

            } else {
                isComplete = false;
            }
        }
        return isComplete;
    }

    /**
     * Mapping time table boolean.
     *
     * @param scheduleArray    the schedule array
     * @param subjectCreditMap the subject credit map
     * @return the boolean
     */
// 랜덤하게 생긴 시간표를 각 학점에 맞게 일정 횟수씩 제거하여 정식 시간표 만들기
    // 1. 해당 교시에 중복되지 않은 과목의 갯수를 센다.
    // 2. 해당 교시에 중복되지 않은 과목의 갯수가 많은 과목들은 중복된 해당 교시에서 제거한다.
    //  2.1. 2 학점 짜리 과목 우선으로 제거한다. 두 번째
    //  2.2. 해당 학점이 2 학점이면 1개가 기준
    //  2.3. 해당 학점이 3 학점이면 2개가 기준
    public boolean mappingTimeTable(String[][] scheduleArray, Map<String, Integer> subjectCreditMap) {
        boolean isMapping = false;
        Map<String, Integer> checkSubjectCreditMap = new HashMap<String, Integer>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                String target = scheduleArray[i][j];

                // 빈 교시와 중복된 곳은 빼고 매핑된 과목을 찾아서 카운트를 센다.
                if (!"x".equals(target) && target.length() == 1) {
                    int count = 0;
                    if (checkSubjectCreditMap.get(target) == null) {
                        checkSubjectCreditMap.put(target, count);

                    } else {
                        checkSubjectCreditMap.put(target, ++count);
                    }

                }
            }
        }

        boolean isCheck = true;
        for (String key : subjectCreditMap.keySet()) {
            for (String checkKey : checkSubjectCreditMap.keySet()) {
                int count = checkSubjectCreditMap.get(checkKey);

                if (key.equals(checkKey)) {
                    if (count < 1) {
                        isMapping = false;
                        break;
                    }
                }
            }
        }

        if(isCheck) {
            isMapping = true;
        }
        return isMapping;
    }

    /**
     * Create time table.
     *
     * @param scheduleArray    the schedule array
     * @param subjectCreditMap the subject credit map
     */
// 중복된 것들을 하나씩 제거하며 시간표를 세운다.
    public void createTimeTable(String[][] scheduleArray, Map<String, Integer> subjectCreditMap) {
        Map<String, Integer> checkSubjectCreditMap = new HashMap<String, Integer>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                String target = scheduleArray[i][j];

                // 해당 과목을 빈 교시를 제외한 모든 경우에서 찾아 카운트를 세어 놓는다.
                if (!"x".equals(target)) {
                    int count = 0;

                    // 해당 교시가 중복된 수업이 있을 경우
                    if (target.length() > 1) {
                        String[] split = target.split(",");
                        String standard = split[0];
                        String secondStandard = split[1];

                        int index = target.indexOf(standard);

                        // 앞의 과목이 기준인 경우
                        // 1. 두 과목 전부 최초이거나 앞의 과목만 최초일 경우
                        // 2. 두 과목 전부 최초가 아니고 카운트가 만료되지 않았을 때는 앞의 것을 기준
                        // 3. 앞 과목이 최초가 아니나 카운트가 만료되지 않았을 경우에는 무조건 앞의 것을 기준
                        if ((checkSubjectCreditMap.get(standard) == null && checkSubjectCreditMap.get(secondStandard) == null)
                                || (checkSubjectCreditMap.get(standard) == null && checkSubjectCreditMap.get(secondStandard) != null)
                                || (checkSubjectCreditMap.get(standard) != null && subjectCreditMap.get(standard) - checkSubjectCreditMap.get(standard) > 1)) {
                            scheduleArray[i][j] = target.substring(0, index + 1);

                            // 최초이면 ++
                            if(checkSubjectCreditMap.get(standard) == null) {
                                checkSubjectCreditMap.put(standard, ++count);

                                // 최초가 아니면 증가
                            } else {
                                int increase = checkSubjectCreditMap.get(standard) + 1;
                                checkSubjectCreditMap.put(standard, increase);
                            }

                            // 뒤의 과목이 최초일 경우 뒤의 과목을 기준
                        } else if (checkSubjectCreditMap.get(standard) != null && checkSubjectCreditMap.get(secondStandard) == null) {
                            scheduleArray[i][j] = target.substring(index + 2, target.length());

                            // 최초이면 ++
                            if(checkSubjectCreditMap.get(secondStandard) == null) {
                                checkSubjectCreditMap.put(secondStandard, ++count);

                                // 최초가 아니면 증가
                            } else {
                                int increase = checkSubjectCreditMap.get(secondStandard) + 1;
                                checkSubjectCreditMap.put(secondStandard, increase);
                            }

                            // 최초가 아니고 앞의 것을 기준으로 잡았으나 앞의 과목이 모든 일정이 채워지고 뒤의 것이 일정이 비어있다면
                        } else if ((checkSubjectCreditMap.get(standard) != null && subjectCreditMap.get(standard) - checkSubjectCreditMap.get(standard) == 1)
                                && (checkSubjectCreditMap.get(secondStandard) != null && subjectCreditMap.get(secondStandard) - checkSubjectCreditMap.get(secondStandard) > 1)) {

                            scheduleArray[i][j] = target.substring(index + 2, target.length());
                            int increase = checkSubjectCreditMap.get(secondStandard) + 1;
                            checkSubjectCreditMap.put(secondStandard, increase);

                            // 최초가 아니고 앞의 것을 기준으로 잡았으나 앞의 과목이 모든 일정이 채워지지 않았을 때
                        } else if ((checkSubjectCreditMap.get(standard) != null && subjectCreditMap.get(standard) - checkSubjectCreditMap.get(standard) > 1)) {
                            scheduleArray[i][j] = target.substring(index + 2, target.length());
                            int increase = checkSubjectCreditMap.get(standard) + 1;
                            checkSubjectCreditMap.put(standard, increase);

                            // 최초가 아니고 앞의 것을 기준으로 잡았으나 앞의 과목이 모든 일정이 채워지고 뒤의 것도 일정이 채워져 있다면 공강으로 만든다.
                        } else if ((checkSubjectCreditMap.get(standard) != null && subjectCreditMap.get(standard) - checkSubjectCreditMap.get(standard) == 1)
                                && (checkSubjectCreditMap.get(secondStandard) != null && subjectCreditMap.get(secondStandard) - checkSubjectCreditMap.get(secondStandard) == 1)) {
                            scheduleArray[i][j] = "x";

                        } else {
                            ;
                        }

                        // 단일 수업일 경우
                    } else {
                        // 최초의 과목이거나
                        if (checkSubjectCreditMap.get(target) == null) {
                            checkSubjectCreditMap.put(target, ++count);

                            // 모든 일정이 채워지지 않은 경우
                        } else if (subjectCreditMap.get(target) - checkSubjectCreditMap.get(target) > 1) {
                            int increase = checkSubjectCreditMap.get(target) + 1;
                            checkSubjectCreditMap.put(target, increase);

                            // 최초가 아니고
                        } else {
                            // 모든 일정이 채워진 경우
                            if (subjectCreditMap.get(target) - checkSubjectCreditMap.get(target) == 1) {
                                scheduleArray[i][j] = "x";
                            }
                        }
                    }
                }
            }
        }

        // 마지막 학점에 대한 배치 카운트 검사
        // 학점에 대한 배치 카운트가 제대로 되지 않은 채 테이블이 만들어지면 올바르지 못하므로
        // 마지막으로 검사한다.
        boolean isCheck = true;
        for(String key : checkSubjectCreditMap.keySet()) {
            if(subjectCreditMap.get(key) - checkSubjectCreditMap.get(key) > 1) {
                isCheck = false;
                break;
            }
        }

        if(isCheck) {
            isOK = true;

        } else {
            isOK = false;
        }
    }
}
