package com.rc.controller;


import com.rc.domain.dto.Result;
import com.rc.service.IRsmPatrolPointService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 巡查点表 前端控制器
 * </p>
 *
 * @author 罗佳炜
 * @since 2024-09-03
 */
@RestController
@RequestMapping("/api/mp")
public class RsmPatrolPointController {
    @Autowired
    private IRsmPatrolPointService patrolPointService;

    @ApiOperation(value = "获取巡查点")
    @RequestMapping("/patrol/checklists/{checklist_id}/items/{item_id}/points/{point_id}")
    @PreAuthorize("hasAuthority('patrol:patrolPoint:query')")
    public Result getPatrolPointByListId(
            @PathVariable("checklist_id") Integer checklistId,
            @PathVariable("item_id") Integer itemId,
            @PathVariable("point_id") Integer pointId
    )
    {
        return patrolPointService.getPatrolPointById(pointId);
    }

    @ApiOperation(value = "获取作业位置")
    @GetMapping("/task/{task_id}/position/{position_id}")
    public Result getTaskPosition(
            @PathVariable("task_id") Integer taskId,
            @PathVariable("position_id") Integer positionId
    )
    {
        return patrolPointService.getTaskPosition(taskId, positionId);
    }
    @ApiOperation(value = "获取巡查点列表")
    @GetMapping("/positions")
    @PreAuthorize("hasAuthority('patrol:patrolPoint:list')")
    public Result getPatrolPointList()
    {
        return patrolPointService.getPatrolPointList();
    }

}
