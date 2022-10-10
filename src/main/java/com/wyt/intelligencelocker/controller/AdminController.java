package com.wyt.intelligencelocker.controller;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.request.*;
import com.wyt.intelligencelocker.service.IAdminService;
import com.wyt.intelligencelocker.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/1 19:26
 * @Email Wei.youting@qq.com
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private IAdminService adminService;

    /**
     * 查询单个用户
     *
     * @param user
     * @return
     */
    @PostMapping("/getUser")
    public Result getUser(@RequestBody QueryUser user) {
        return adminService.getUser(user);
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @PostMapping("/users/{index}")
    public Result getUsers(@PathVariable Integer index) {
        return adminService.getAllUsers(index);
    }

    /**
     * 批量添加站点管理员
     *
     * @param request
     * @return
     */
    @PostMapping("/addManager")
    public Result addManager(@RequestBody AddManagerRequest request) {
        return adminService.addManager(request);
    }

    /**
     * 移除站点管理员
     *
     * @param request
     * @return
     */
    @PostMapping("/removeManager")
    public Result removeManager(@RequestBody RemoveManagerRequest request) {
        return adminService.removeManager(request);
    }

    /**
     * 修改管理站点
     *
     * @param request
     * @return
     */
    @PostMapping("/distribution")
    public Result distribution(@RequestBody ChangeDistributionRequest request) {
        return adminService.distribution(request);
    }

    /**
     * 查询所有管理员
     * @param index
     * @return
     */
    @PostMapping("/managers/{index}")
    public Result getManagers(@PathVariable Integer index) {
        return adminService.getAllManagers(index);
    }

    @PostMapping("/getManager")
    public Result getManager(@RequestBody QueryManagerRequest request){
        return adminService.queryManager(request);
    }

}
