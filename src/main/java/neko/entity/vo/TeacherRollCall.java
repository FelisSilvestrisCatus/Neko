package neko.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 该类是老师点名时用的实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TeacherRollCall {
    private static final long serialVersionUID = 1L;
   /*
     *用户id
    */
   private  int  uid;
    /**
     * 用户名字
     */
    private String uname;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户请假状态
     */
   private  String  state;

}
