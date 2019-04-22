package neko.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuditVacateByTeacher implements Serializable {
    /**
     * 学生姓名，手机号，班级名，课程名，请假类型，请假时间范围，请假原因，请假状态,请假附件
     * vid,uname,phone,classname.coursename,vtype,vtime,vname,state
     */
    private static final long serialVersionUID = 1L;

    /**
     * 请假id
     */

    private Integer vid;

    /**
     * 学生姓名
     */
    private String uname;
    /**
     * 学生编号
     */
    private String uid;
    /**
     * 手机号
     */
    private String phone;

    //班级名
    private String cname;
    //课程名
    private String coursename;
    //请假类型
    private Integer vtype;
    //请假时间范围
    private String vtime;
    //请假原因(用请假编号代替 方便后期查询请假详情====附件获取)
    private String vname;
    //请假状态
    private Integer state;

    //审批备注
    private String remark;


}
