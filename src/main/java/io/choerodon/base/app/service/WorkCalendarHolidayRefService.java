package io.choerodon.base.app.service;

import io.choerodon.base.api.dto.WorkCalendarHolidayRefVO;
import io.choerodon.base.infra.dto.WorkCalendarHolidayRefDTO;

import java.util.List;

/**
 * @author dinghuang123@gmail.com
 * @since 2018/10/9
 */
public interface WorkCalendarHolidayRefService {

    /**
     * 按年份更新日历
     *
     * @param year year
     */
    void updateWorkCalendarHolidayRefByYear(Integer year);

    /**
     * 根据年份查询工作日历
     *
     * @param year year
     * @return WorkCalendarHolidayRefVO
     */
    List<WorkCalendarHolidayRefVO> queryWorkCalendarHolidayRelByYear(Integer year);

    /**
     * 根据年份查询工作日历，包含当年、去年、明年
     *
     * @param year year
     * @return WorkCalendarHolidayRefVO
     */
    List<WorkCalendarHolidayRefVO> queryByYearIncludeLastAndNext(Integer year);
}
