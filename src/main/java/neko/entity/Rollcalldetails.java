package neko.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
public class Rollcalldetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "rdid", type = IdType.AUTO)
    private Integer rdid;

    /**
     * 点名id
     */
    private Integer rid;

    /**
     * 学生id
     */
    private Integer uid;

    /**
     * 点名时间
     */
    private LocalDateTime date;


}
