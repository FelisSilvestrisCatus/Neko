package neko.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VacateWithTeacherName implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 请假原因
     */
    private String vname;

    /**
     * 请假类型(0为事假,1为病假,3为其他)
     */
    private Integer vtype;

    /**
     * 状态(0为已申请,1为未批准,2为已批准)
     */
    private Integer state;

    /**
     * 学生请假时间
     */
    private String vtime;

    /**
     * 老师姓名
     */
    private String uname;
    /**
     * 班级名
     */
    private String cname;

}
