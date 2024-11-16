package com.medic.system.dtos.sick_leave;

import com.medic.system.entities.Doctor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthWithSickLeaveCount {
    private Integer month;
    private Long sickLeaveCount;

    public MonthWithSickLeaveCount(Integer month, Long sickLeaveCount) {
        this.month = month;
        this.sickLeaveCount = sickLeaveCount;
    }

    public String getMonthText()
    {
        switch (month)
        {
            case 1:
                return "Януари";
            case 2:
                return "Февруари";
            case 3:
                return "Март";
            case 4:
                return "Април";
            case 5:
                return "Май";
            case 6:
                return "Юни";
            case 7:
                return "Юли";
            case 8:
                return "Август";
            case 9:
                return "Септември";
            case 10:
                return "Октомври";
            case 11:
                return "Ноември";
            case 12:
                return "Декември";
            default:
                return "";
        }
    }
}
