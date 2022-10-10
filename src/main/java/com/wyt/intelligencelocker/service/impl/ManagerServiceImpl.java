package com.wyt.intelligencelocker.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.wyt.intelligencelocker.annotation.CheckPermission;
import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.dao.RiderMapper;
import com.wyt.intelligencelocker.dao.UserMapper;
import com.wyt.intelligencelocker.dao.UserOperationMapper;
import com.wyt.intelligencelocker.entity.Do.RiderDo;
import com.wyt.intelligencelocker.entity.Do.UserDo;
import com.wyt.intelligencelocker.entity.Do.UserOperationDo;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.enums.RiderLevelEnum;
import com.wyt.intelligencelocker.meta.enums.UserRoleEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.meta.request.AddRiderRequest;
import com.wyt.intelligencelocker.meta.request.QueryRiderRequest;
import com.wyt.intelligencelocker.meta.request.RemoveRiderRequest;
import com.wyt.intelligencelocker.service.IManagerService;
import com.wyt.intelligencelocker.utils.JwtUtil;
import com.wyt.intelligencelocker.utils.ValidateExtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/9 16:22
 * @Email Wei.youting@qq.com
 */
@Service
public class ManagerServiceImpl implements IManagerService {

    private final Integer DEFAULT_ORDER_NUM = 0;
    private final Double DEFAULT_COMMENT = 0.0;

    @Resource
    private RiderMapper riderMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserOperationMapper operationMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CheckPermission(UserRoleEnum.MANAGER)
    public Result addRider(AddRiderRequest request) {
        // 校验参数
        ValidateExtUtil.validate(request);

        // 判断用户是否存在
        UserDo user = userMapper.selectUserByPhone(request.getPhone());
        if (ObjUtil.isEmpty(user)) {
            throw new GlobalException(ResultCodeEnum.PHONE_NOT_REGSTER);
        }

        // 判断骑手是否已经存在
        if (ObjUtil.isNotEmpty(riderMapper.countByPhone(request.getPhone()))) {
            throw new GlobalException(ResultCodeEnum.RIDER_EXIST);
        }

        // 添加骑手
        RiderDo rider = RiderDo.builder()
                .phone(request.getPhone())
                .orderNum(DEFAULT_ORDER_NUM)
                .comment(DEFAULT_COMMENT)
                .level(RiderLevelEnum.ROOKIE.getCode())
                .build();
        if (!(riderMapper.insertRider(rider) > 0)) {
            throw new GlobalException(ResultCodeEnum.ADD_RIDER_FAIL);
        }

        // 更新用户表
        if (!(userMapper.updateUserRole(UserRoleEnum.RIDER.getCode(), request.getPhone()) > 0)) {
            throw new GlobalException(ResultCodeEnum.ADD_RIDER_FAIL);
        }

        //获取当前登录的手机号
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // 保存操作记录
        UserOperationDo info = UserOperationDo.builder().phone(request.getPhone())
                .operationInfo(StrUtil.format("修改权限：{} -> {}", UserRoleEnum.getBycode(user.getRole()).getMsg(), UserRoleEnum.RIDER.getMsg()))
                .operationIp(httpServletRequest.getRemoteAddr())
                .operationUser(userPhone)
                .operationTime(new Date())
                .build();
        if (!(operationMapper.insertInfo(info) > 0)) {
            throw new GlobalException(ResultCodeEnum.ADD_RIDER_FAIL);
        }

        return new Result(ResultCodeEnum.ADD_RIDER_SUCCESS);
    }

    @Override
    @CheckPermission(UserRoleEnum.MANAGER)
    @Transactional(rollbackFor = Exception.class)
    public Result removeRider(RemoveRiderRequest request) {
        // 校验参数
        ValidateExtUtil.validate(request);

        // 判断用户是否存在
        UserDo user = userMapper.selectUserByPhone(request.getPhone());
        if (ObjUtil.isEmpty(user)) {
            throw new GlobalException(ResultCodeEnum.PHONE_NOT_REGSTER);
        }

        // 判断骑手是否已经存在
        if (ObjUtil.isEmpty(riderMapper.countByPhone(request.getPhone()))) {
            throw new GlobalException(ResultCodeEnum.RIDER_NOT_EXIST);
        }

        if (!(riderMapper.removeByPhone(request.getPhone()) > 0)) {
            throw new GlobalException(ResultCodeEnum.REMOVE_RIDER_FAIL);
        }

        // 更新用户表
        if (!(userMapper.updateUserRole(UserRoleEnum.USER.getCode(), request.getPhone()) > 0)) {
            throw new GlobalException(ResultCodeEnum.REMOVE_RIDER_FAIL);
        }

        //获取当前登录的手机号
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // 保存操作记录
        UserOperationDo info = UserOperationDo.builder().phone(request.getPhone())
                .operationInfo(StrUtil.format("修改权限：{} -> {}", UserRoleEnum.getBycode(user.getRole()).getMsg(), UserRoleEnum.USER.getMsg()))
                .operationIp(httpServletRequest.getRemoteAddr())
                .operationUser(userPhone)
                .operationTime(new Date())
                .build();
        if (!(operationMapper.insertInfo(info) > 0)) {
            throw new GlobalException(ResultCodeEnum.REMOVE_RIDER_FAIL);
        }

        return new Result(ResultCodeEnum.REMOVE_RIDER_SUCCESS);
    }

    @Override
    @CheckPermission(UserRoleEnum.MANAGER)
    public Result queryAll() {

        List<RiderDo> riderDos = riderMapper.queryAllRider();

        return new Result(ResultCodeEnum.QUERY_USER_SUCCESS, riderDos);
    }

    @Override
    @CheckPermission(UserRoleEnum.MANAGER)
    public Result queryByPhone(QueryRiderRequest request) {
        //校验参数
        ValidateExtUtil.validate(request);

        RiderDo riderDo = riderMapper.queryByPhone(request.getPhone());

        return new Result(ResultCodeEnum.QUERY_USER_SUCCESS, riderDo);

    }
}
