package neko.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ClassWithTeacherName implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 班级id
     */
    @TableId(value = "cid", type = IdType.AUTO)
    private Integer cid;

    /**
     * 班级名
     */
    private String cname;

    /**
     * 班级状态，0未初始化，1正常，2冻结
     */
    private Integer cstate;

    //班级老师
    private String uname;


}
