package neko.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VacateDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请假id
     */
    private Integer vid;

    /**
     * 请假类型
     */
    private int vtype;
    /**
     * 请假附件id
     */
    private Integer vfid;

    /**
     * 请假时所用的附件
     */
    private String  filepath;

}
