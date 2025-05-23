package com.rc.service;

import com.rc.domain.dto.Result;
import com.rc.domain.entity.RsmSnapshot;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 随手拍问题表 服务类
 * </p>
 *
 * @author 罗佳炜
 * @since 2024-09-03
 */
public interface IRsmSnapshotService extends IService<RsmSnapshot> {

    Result getSnapshotList(Integer pageNumber, Integer pageSize, Integer property);

    Result getSnapshotById(Long id);

    Result addSnapshot(RsmSnapshot rsmSnapshot);

    Result doneSnapshot(Long id, RsmSnapshot rsmSnapshot);
}
