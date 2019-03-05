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
 * @since 2019-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

    /**
     * 姓名
     */
    private String uname;

    /**
     * 用户密码
     */
    private String pwd;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 0为管理员，1为教师，2为学生
     */
    private Integer type;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 证件号码（学生证，教师证）
     */
    private Integer idnumber;

    /**
     * 0为启用,1为禁用
     */
    private Integer flag;


}
