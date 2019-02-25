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
 * @since 2019-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

    private String uname;

    private String pwd;

    private String phone;

    /**
     * 0为管理员，1为教师，2为学生
     */
    private Integer type;

    private String email;

    /**
     * 证件号码（学生证，教师证）
     */
    private Integer idnumber;

    /**
     * 用户标识
     */
    private String token;


}
