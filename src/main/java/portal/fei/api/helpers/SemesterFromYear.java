package portal.fei.api.helpers;

import portal.fei.api.domain.isp.StudentRequest;

public class SemesterFromYear {

    public static String getSemesterFromYear(StudentRequest studentRequest){
        return SemesterFromYear.getSemester(studentRequest) + " " + SemesterFromYear.getAkYear(studentRequest);
    }

    public static String getAkYear(StudentRequest studentRequest){
        var createdAt = studentRequest.getCreatedAt();
        int exact_year = createdAt.getYear() + 1900;
        int exact_month = createdAt.getMonth() + 1;

        if(exact_month <= 7){
            return  (Integer.toString(exact_year - 1)+"/"+Integer.toString(exact_year));
        }

        return (Integer.toString(exact_year)+"/"+Integer.toString(exact_year + 1));
    }

    public static String getSemester(StudentRequest studentRequest){
        var createdAt = studentRequest.getCreatedAt();

        int exact_month = createdAt.getMonth() + 1;

        if(exact_month >= 2 && exact_month <= 7){
            return  "LS";
        }

        return "ZS";
    }
}
