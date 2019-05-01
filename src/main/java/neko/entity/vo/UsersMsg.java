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
 * @since 2019-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UsersMsg implements Serializable {

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
    private String mtime;

    /**
     * 消息状态(0未读,1已读)
     */
    private Integer mstate;

    /**
     * 消息发送者姓名
     */
    private String uname;

}
