package neko.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author z9961
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentCourseName implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程id
     */
    private Integer courseid;

    /**
     * 班级名
     */
    private String classname;

    /**
     * 课程名
     */
    private String cname;


    /**
     * 任课老师
     */
    private String uname;

    /**
     * 星期几
     */
    private Integer cday;

    /**
     * 状态位
     */
    private Integer state;
    /**
     * 班级总人数
     */
    private int snum;
    /**
     * 该课程应到人数
     */
    private int anum;

    //===========================图标所需要的数据======================================//
    private int attnum;//喊道的人
    private int notattnum;//逃课的人
    private LocalDateTime retime;//点名时间
    private int vnum;//请假的人数


}
