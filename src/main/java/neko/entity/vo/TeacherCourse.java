package neko.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 课程表
 * </p>
 *
 * @author z9961
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TeacherCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程id
     */
    @TableId(value = "courseid", type = IdType.AUTO)
    private Integer courseid;

    /**
     * 班级id
     */
    private Integer cid;

    /**
     * 教师id
     */
    private Integer tid;

    /**
     * 课程名
     */
    private String cname;

    /**
     * 星期几
     */
    private Integer cday;

    /**
     * 状态位
     */
    private Integer state;


}
