package neko.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 请假表
 * </p>
 *
 * @author z9961
 * @since 2019-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Vacate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "vid", type = IdType.AUTO)
    private Integer vid;

    /**
     * 学生id
     */
    private Integer uid;

    /**
     * 课程id
     */
    private Integer courseid;

    /**
     * 教师id
     */
    private Integer tid;

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
     * 审批备注
     */
    private String remark;


}
