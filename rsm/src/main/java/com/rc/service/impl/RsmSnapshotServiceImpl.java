package com.rc.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rc.domain.dto.Result;
import com.rc.domain.dto.SnapShotList;
import com.rc.domain.dto.UserDTO;
import com.rc.domain.entity.RsmSnapshot;
import com.rc.mapper.RsmSnapshotMapper;
import com.rc.service.IRsmSnapshotService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.utils.AliOssUtil;
import com.rc.utils.CacheClient;
import com.rc.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.rc.utils.RedisConstants.*;


/**
 * <p>
 * 随手拍问题表 服务实现类
 * </p>
 *
 * @author 罗佳炜
 * @since 2024-09-03
 */
@Service
public class RsmSnapshotServiceImpl extends ServiceImpl<RsmSnapshotMapper, RsmSnapshot> implements IRsmSnapshotService {


    @Autowired
    private  RsmSnapshotMapper rsmSnapshotMapper;
    @Autowired
    private CacheClient cacheClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result getSnapshotList(Integer pageNumber, Integer pageSize, Integer property) {
        // 创建分页对象
        Page<RsmSnapshot> page = new Page<>(pageNumber, pageSize);

        // 调用 Mapper 方法，执行分页查询
        IPage<RsmSnapshot> snapShotListPage = rsmSnapshotMapper.getSnapShotList(page, property);
        SnapShotList snapShotList = new SnapShotList (snapShotListPage.getRecords(), snapShotListPage.getTotal());
        snapShotList.setTotal((long) snapShotList.getSnapshotListData().size());
        // 将结果封装到 Result 对象中返回
        return Result.ok("获取成功",snapShotList);
    }

    private RsmSnapshot getByIdWithCache(Long id) {
        //缓存优化
        return cacheClient.queryWithPassThrough(
                SNAPSHOTS_KEY,
                id,
                RsmSnapshot.class,
                this::getById,
                CACHE_TTL,
                TimeUnit.MINUTES
        );
    }

    @Override
    public Result getSnapshotById(Long id) {
        RsmSnapshot rsmSnapshot = this.getByIdWithCache(id);
        if (rsmSnapshot==null) {
            return Result.fail("随手拍获取失败");
        }
        return Result.ok("获取成功",rsmSnapshot);
    }

    @Override
    public Result addSnapshot(RsmSnapshot rsmSnapshot) {
        UserDTO user = UserHolder.getUser();
        Long userId = user.getUserId();
//        Long userId=2L;
        rsmSnapshot.setCreatorId(userId);
        //更新时间
        rsmSnapshot.setCreateTime(LocalDateTime.now());
        rsmSnapshot.setUpdateTime(LocalDateTime.now());
        //正式添加进去
        boolean saved = this.save(rsmSnapshot);
        if (!saved) {
            return Result.fail("添加失败");
        }
        return Result.ok("添加成功",this.getByIdWithCache(rsmSnapshot.getId()));
    }

    @Override
    public Result doneSnapshot(Long id, RsmSnapshot rsmSnapshot) {
        UserDTO user = UserHolder.getUser();
        Long userId = user.getUserId();
//        Long userId = 2L;
        //更新时间
        rsmSnapshot.setHandlerTime(LocalDateTime.now());
        rsmSnapshot.setHandlerId(userId);
        rsmSnapshot.setUpdateTime(LocalDateTime.now());
//        //查询rsm
//        RsmSnapshot OldRsmSnapshot = this.getByIdWithCache(id);
//        if (OldRsmSnapshot==null) {
//            return Result.fail("数据不存在请重新查询");
//        }
//        //获取旧数据中的url
//        String imgPath = OldRsmSnapshot.getImgPath();
//        //处理Url以逗号分割进入列表
//        String[] imgPaths = imgPath.split(",");
//        //再开一个线程用于删除imgPaths
//        new Thread(() -> {
//            Arrays.stream(imgPaths).forEach(path -> {
//                //删除图片
//                try {
//                    AliOssUtil.deleteFile(path);
//                    System.out.println("已经删除:"+path);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }).start();
        //正式修改
        int updated = rsmSnapshotMapper.handelSnapshot(rsmSnapshot,id);
        if (updated<=0) {
            return Result.fail("修改失败");
        }
        //删除缓存
        stringRedisTemplate.delete(SNAPSHOTS_KEY + id);
        return Result.ok("修改成功",this.getByIdWithCache(rsmSnapshot.getId()));
    }
}
