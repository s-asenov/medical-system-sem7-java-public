package com.medic.system.dtos.sick_leave;

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
        return switch (month) {
            case 1 -> "Януари";
            case 2 -> "Февруари";
            case 3 -> "Март";
            case 4 -> "Април";
            case 5 -> "Май";
            case 6 -> "Юни";
            case 7 -> "Юли";
            case 8 -> "Август";
            case 9 -> "Септември";
            case 10 -> "Октомври";
            case 11 -> "Ноември";
            case 12 -> "Декември";
            default -> "";
        };
    }
}
