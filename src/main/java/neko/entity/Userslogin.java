package neko.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Userslogin implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer uid;

    @TableField("loginTime")
    private LocalDateTime loginTime;

    @TableField("loginIp")
    private String loginIp;

    @TableField("loginType")
    private Integer loginType;


}
