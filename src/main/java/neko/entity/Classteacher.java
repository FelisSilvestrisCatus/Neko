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
public class Classteacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ctid", type = IdType.AUTO)
    private Integer ctid;

    private Integer cid;

    private Integer uid;


}
