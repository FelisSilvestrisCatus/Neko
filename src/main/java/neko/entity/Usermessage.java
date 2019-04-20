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
 * @since 2019-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Usermessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "umid", type = IdType.AUTO)
    private Integer umid;

    /**
     * 用户
     */
    private Integer uid;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 发送消息的时间
     */
    private LocalDateTime mtime;

    /**
     * 消息状态(0未读,1已读)
     */
    private Integer mstate;

    /**
     * 消息发送者uid
     */
    private Integer sourceid;


}
