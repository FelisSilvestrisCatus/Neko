package neko.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentRollcall implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 点名详情id
     */
    @TableId(value = "rdid", type = IdType.AUTO)
    private Integer rdid;

    /**
     * 班级名
     */
    private String cname;

    /**
     * 课程名
     */
    private String coursename;


    /**
     * 老师姓名
     */
    private String uname;

    /**
     * 点名时间
     */
    private LocalDateTime rtime;

    /**
     * 0出勤,1缺勤
     */
    private Integer state;
}
