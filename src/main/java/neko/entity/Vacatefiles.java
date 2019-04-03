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
 * @since 2019-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Vacatefiles implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "vfid", type = IdType.AUTO)
    private Integer vfid;

    /**
     * 请假id
     */
    private Integer vid;

    /**
     * 附件路径
     */
    private String filepath;


}
