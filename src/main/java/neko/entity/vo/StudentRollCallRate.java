package neko.entity.vo;

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
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentRollCallRate implements Serializable {

    private static final long serialVersionUID = 1L;
    private int uid;
    private String uname;

    //===========================图标所需要的数据======================================//
    private int attnum;//出勤次数
    private int notattnum;//不在的次数（不含请假）
    private int attrate;//比例
    private  String  phone;


}
