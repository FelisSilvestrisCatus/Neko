package neko.entity.vo;

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
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentCourseName implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 课程id
     */
    private Integer courseid;
    /**
     * 课程名
     */
    private String cname;


}
