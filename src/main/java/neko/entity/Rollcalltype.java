package neko.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Rollcalltype implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 点名类型id
     */
    @TableId(value = "rtid", type = IdType.AUTO)
    private Integer rtid;

    /**
     * 类型名称
     */
    private String rtname;

    /**
     * 详细说明
     */
    private String rtdescription;


}
