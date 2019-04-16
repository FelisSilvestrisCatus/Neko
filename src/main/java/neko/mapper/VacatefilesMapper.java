package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.entity.Vacatefiles;
import neko.entity.vo.AuditVacateByTeacher;
import neko.entity.vo.VacateDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-03-31
 */
public interface VacatefilesMapper extends BaseMapper<Vacatefiles> {
    @Select("select vacate.vid, vacate.vtype,vacatefiles.filepath from " +
            "vacate vacate left join vacatefiles vacatefiles" +
            " on vacate.vid = vacatefiles.vid where vacate.vid=#{vid}")
    VacateDetail getVacateDetail(int vid);
}
