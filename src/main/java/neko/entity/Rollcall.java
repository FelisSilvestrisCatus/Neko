package neko.entity;

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
 * @since 2019-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Rollcall implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 点名id
     */
    @TableId(value = "rid", type = IdType.AUTO)
    private Integer rid;

    /**
     * 课程id
     */
    private Integer cid;

    /**
     * 点名时间
     */
    private LocalDateTime rtime;

    /**
     * 点名方式
     */
    private Integer rtid;


}
