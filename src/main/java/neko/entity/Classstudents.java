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
 * @since 2019-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Classstudents implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "csid", type = IdType.AUTO)
    private Integer csid;

    /**
     * 班级id
     */
    private Integer cid;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 0待审核,1已加入
     */
    private Integer state;


}
