package neko.entity;

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
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Studentcourse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生课程主键
     */
    @TableId(value = "scid", type = IdType.AUTO)
    private Integer scid;

    /**
     * 学生id
     */
    private Integer sid;

    /**
     * 课程id
     */
    private Integer cid;


}
