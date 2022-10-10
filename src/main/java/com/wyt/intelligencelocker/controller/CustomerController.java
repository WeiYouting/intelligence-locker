package com.wyt.intelligencelocker.controller;

import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.service.ICustomerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author WeiYouting
 * @create 2022/10/10 9:04
 * @Email Wei.youting@qq.com
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Resource
    private ICustomerService customerService;

    /**
     * 查询所有订单
     * @return
     */
    @PostMapping("/queryAllOrder/{index}")
    public Result queryAllOrder(@PathVariable Integer index){
        return customerService.queryAllOrder(index);
    }

    /**
     * 查询未完成的订单
     * @return
     */
    @PostMapping("/queryOrder")
    public Result queryUnfinshed(){
        return customerService.queryUnfinshed();
    }

    /**
     * 取货
     * @return
     */
    @PostMapping("/get")
    public Result get(){
        return customerService.get();
    }


}
