package neko.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Rollcalldetails;
import neko.entity.Users;
import neko.entity.vo.StudentRollcall;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
public interface IRollcalldetailsService extends IService<Rollcalldetails> {

    Map<String, String> myInfo(Users u);

    IPage<StudentRollcall> myAttendance(Integer uid, Integer currentPage);
}
