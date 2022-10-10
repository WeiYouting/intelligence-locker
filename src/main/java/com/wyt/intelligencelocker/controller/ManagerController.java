package com.wyt.intelligencelocker.controller;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.meta.request.AddRiderRequest;
import com.wyt.intelligencelocker.meta.request.QueryRiderRequest;
import com.wyt.intelligencelocker.meta.request.RemoveRiderRequest;
import com.wyt.intelligencelocker.service.IManagerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author WeiYouting
 * @create 2022/10/9 16:17
 * @Email Wei.youting@qq.com
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Resource
    private IManagerService managerService;

    /**
     * 添加骑手
     *
     * @param request
     * @return
     */
    @PostMapping("/addRider")
    public Result addRider(@RequestBody AddRiderRequest request) {
        return managerService.addRider(request);
    }

    /**
     * 删除骑手
     * @param request
     * @return
     */
    @PostMapping("/removeRider")
    public Result removeRider(@RequestBody RemoveRiderRequest request) {
        return managerService.removeRider(request);
    }

    /**
     * 查询所有骑手
     * @return
     */
    @PostMapping("/queryAll")
    public Result queryAll(){
        return managerService.queryAll();
    }

    /**
     * 查找骑手
     * @param request
     * @return
     */
    @PostMapping("/query")
    public Result queryByPhone(@RequestBody QueryRiderRequest request){
        return managerService.queryByPhone(request);
    }


}
