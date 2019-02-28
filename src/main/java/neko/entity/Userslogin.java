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
 * @since 2019-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Userslogin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "ulid", type = IdType.AUTO)
    private Integer ulid;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 登陆时间
     */
    private LocalDateTime logintime;

    /**
     * 登录ip，用于判断登录地区
     */
    private String loginip;

    /**
     * 登录类型，1为web，2为移动端，3为桌面客户端，4微信端，5安卓客户端
     */
    private Integer logintype;

    /**
     * 登录地区
     */
    private String loginlocation;


}
