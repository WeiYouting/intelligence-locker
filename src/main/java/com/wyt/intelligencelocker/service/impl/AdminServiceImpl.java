package com.wyt.intelligencelocker.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.anno.Cached;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wyt.intelligencelocker.annotation.CheckPermission;
import com.wyt.intelligencelocker.controller.util.CodeUtil;
import com.wyt.intelligencelocker.controller.util.PageResult;
import com.wyt.intelligencelocker.controller.util.Result;
import com.wyt.intelligencelocker.dao.*;
import com.wyt.intelligencelocker.entity.Do.*;
import com.wyt.intelligencelocker.entity.Vo.ManagerVo;
import com.wyt.intelligencelocker.entity.Vo.UserVo;
import com.wyt.intelligencelocker.meta.enums.ResultCodeEnum;
import com.wyt.intelligencelocker.meta.enums.UserRoleEnum;
import com.wyt.intelligencelocker.meta.enums.UserStatusEnum;
import com.wyt.intelligencelocker.meta.exception.GlobalException;
import com.wyt.intelligencelocker.meta.request.*;
import com.wyt.intelligencelocker.service.IAdminService;
import com.wyt.intelligencelocker.utils.*;
import org.apache.catalina.Manager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author WeiYouting
 * @create 2022/10/1 19:32
 * @Email Wei.youting@qq.com
 */

@Service
public class AdminServiceImpl implements IAdminService {

    private final Integer PAGE_NUM = 10;

    @Resource
    private ManagerMapper managerMapper;

    @Resource
    private RoleUtil roleUtil;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserOperationMapper userOperationMapper;

    @Resource
    private CodeUtil codeUtil;

    @Resource
    private CacheUtil cacheService;

    @Resource
    private ManagerOperationMapper managerOperationMapper;

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private JwtUtil jwtUtil;

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    /**
     * ??????????????????(???????????????????????????)
     *
     * @return
     */
    @Override
    @CheckPermission(UserRoleEnum.ADMIN)
    public Result getAllUsers(Integer index) {
        // ??????pagehelper??????
        PageHelper.startPage(index, PAGE_NUM);

        //??????????????????
        List<UserDo> users = userMapper.selectAllUser();
        Page page = (Page) users;

        // DO -> VO
        ArrayList<UserVo> userVos = new ArrayList<>();
        users.forEach(userDo -> {
            userVos.add(UserVo.builder().phone(userDo.getPhone()).name(userDo.getName())
                    .role(UserRoleEnum.getBycode(userDo.getRole()).getMsg())
                    .registerTime(simpleDateFormat.format(userDo.getRegisterTime()))
                    .lastOnlineTime(simpleDateFormat.format(userDo.getLastOnlineTime()))
                    .integral(userDo.getIntegral()).status(UserStatusEnum.getByCode(userDo.getStatus()).getMsg())
                    .build());
        });

        // ??????????????????
        PageResult result = PageResult.builder().pages(page.getPages())
                .pageSize(page.getPageSize())
                .pageIndex(page.getPageNum())
                .startRow(page.getStartRow())
                .endRow(page.getEndRow())
                .total(page.getTotal())
                .data(userVos).build();

        return new Result(ResultCodeEnum.QUERY_ALL_USER_SUCCESS, result);
    }

    /**
     * ????????????
     *
     * @param request
     * @return
     */
    @Override
    @CheckPermission(UserRoleEnum.ADMIN)
    @Cached(name = "userInfo::", key = "#request.phone", expire = 3600)
    public Result getUser(QueryUser request) {
        // ???????????????????????????
        UserDo user = userMapper.selectUserByPhone(request.getPhone());

        // Do -> Vo
        UserVo userVo = UserVo.builder().phone(user.getPhone()).name(user.getName())
                .role(UserRoleEnum.getBycode(user.getRole()).getMsg())
                .registerTime(user.getRegisterTime().toString())
                .lastOnlineTime(user.getLastOnlineTime().toString())
                .integral(user.getIntegral())
                .status(UserStatusEnum.getByCode(user.getStatus()).getMsg()).build();

        return new Result(ResultCodeEnum.QUERY_USER_SUCCESS, userVo);
    }

    /**
     * ?????????????????????
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @CheckPermission(UserRoleEnum.ADMIN)
    public Result addManager(AddManagerRequest request) {
        //????????????
        ValidateExtUtil.validate(request);

        //??????????????????????????????
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // ???????????????????????????
        if (!(request.getCode().equals(codeUtil.check(userPhone)))) {
            throw new GlobalException(ResultCodeEnum.CODE_ERROR);
        }

        // ?????????????????????????????????????????????
        List<String> phoneList = request.getUserList().stream().map(v -> v.getPhone()).collect(Collectors.toList());

        // ???????????????????????????
        if (!(userMapper.selectNumberByPhones(phoneList) == phoneList.size())) {
            throw new GlobalException(ResultCodeEnum.PHONE_NOT_REGSTER);
        }

        // ????????????????????? ????????????????????????????????????
        List<String> adminList = userMapper.selectAllAdmin();
        adminList.forEach(admin -> {
            if (phoneList.contains(admin)) {
                throw new GlobalException(ResultCodeEnum.ADMIN_NOT_CHANGE);
            }
        });

        // ???????????????????????????????????????
        List<ManagerDo> managerList = managerMapper.selectAllManager();
        managerList.stream().forEach(manager -> {
            if (phoneList.contains(manager.getPhone())) {
                throw new GlobalException(ResultCodeEnum.ADD_MANAGER_ALREADY, manager.getPhone());
            }
        });

        // ???????????????????????????????????????(????????????????????????)
        List<Integer> roleList = userMapper.selectRoleByPhones(phoneList);
        HashMap<String, Integer> roleMap = new HashMap<>();
        Iterator<Integer> iterator = roleList.iterator();
        phoneList.forEach(phone -> {
            roleMap.put(phone, iterator.next());
        });

        // ???????????????????????????????????????
        if (!(userMapper.updateManager(phoneList) == phoneList.size())) {
            throw new GlobalException(ResultCodeEnum.ADD_MANAGER_FAIL);
        }

        // ??????????????????????????????????????????
        if (!(managerMapper.insertManager(request.getUserList()) == phoneList.size())) {
            throw new GlobalException(ResultCodeEnum.ADD_MANAGER_FAIL);
        }

        ArrayList<UserOperationDo> userOperationDos = new ArrayList<>();
        // ??????????????????
        phoneList.stream().forEach(phone -> {
            userOperationDos.add(new UserOperationDo().builder().phone(phone)
                    .operationTime(new Date())
                    .operationIp(httpServletRequest.getRemoteAddr())
                    .operationUser(userPhone)
                    .operationInfo(StrUtil.format(UserOperationUtil.changeRoleTemplate, UserRoleEnum.getBycode(roleMap.get(phone)).getMsg(), UserRoleEnum.MANAGER.getMsg()))
                    .build());
        });

        // ???????????????????????????????????????
        if (!(userOperationMapper.insertInfos(userOperationDos) == phoneList.size())) {
            throw new GlobalException(ResultCodeEnum.FAIL);
        }

        // ???????????????????????????
        cacheService.SMSRemove(userPhone);
        return new Result(ResultCodeEnum.ADD_MANAGER_SUCCESS);
    }

    @Override
    @CheckPermission(UserRoleEnum.ADMIN)
    public Result queryManager(QueryManagerRequest request) {
        List<ManagerVo> managerList = new ArrayList<>();

        // ??????????????????????????? ?????????????????????????????????
        if (StrUtil.isNotBlank(request.getPhone())) {

            // ??????????????????????????????????????????
            ManagerDo manager = managerMapper.queryByPhone(request.getPhone());
            if (ObjUtil.isEmpty(manager)) {
                throw new GlobalException(ResultCodeEnum.NOT_MANAGER);
            }

            // ??????????????????
            List<SiteDo> siteDos = siteMapper.queryAllSite();

            // ??????????????????map key?????????id
            Map<String, SiteDo> siteDoMap = siteDos.stream().collect(Collectors.toMap(siteDo -> siteDo.getId().toString(), Function.identity(), (key1, key2) -> key2));

            // ????????????id????????????
            if (ObjUtil.isNotEmpty(manager.getSiteId()) && siteDos.stream().anyMatch(site -> site.getId().equals(manager.getSiteId()))) {

                // ?????????????????? ???????????????
                SiteDo site = siteDoMap.get(manager.getSiteId().toString());
                managerList.add(
                        ManagerVo.builder()
                                .phone(manager.getPhone())
                                .siteName(site.getSiteName())
                                .siteAddress(site.getSiteAddress())
                                .province(site.getProvince())
                                .city(site.getCity())
                                .build());
            }else{
                managerList.add(
                        ManagerVo.builder()
                                .phone(manager.getPhone())
                                .build());
            }
        }

        // ????????????????????? ???????????????????????????
        if (StrUtil.isNotBlank(request.getProvince())) {
            List<SiteDo> siteDos = siteMapper.queryByProvince(request.getProvince());
            managerList.addAll(queryFor(siteDos));
        }

        // ????????????????????? ???????????????????????????
        if (StrUtil.isNotBlank(request.getCity())) {
            List<SiteDo> siteDos = siteMapper.queryByCity(request.getCity());
            managerList.addAll(queryFor(siteDos));
        }

        // ??????????????????????????? ?????????????????????????????????
        if (StrUtil.isNotBlank(request.getSiteName())) {
            List<SiteDo> siteDos = siteMapper.queryBySiteName(request.getSiteName());
            managerList.addAll(queryFor(siteDos));
        }

        // ????????????
        List<ManagerVo> data = managerList.stream().distinct().collect(Collectors.toList());
            return new Result(ResultCodeEnum.QUERY_MANAGER_SUCCESS, data);
    }

    /**
     * ???????????????????????????????????????
     * @param siteDos
     * @return
     */
    private List<ManagerVo> queryFor(List<SiteDo> siteDos) {
        ArrayList<ManagerVo> managerList = new ArrayList<>();

        // ???????????????????????? key?????????id value???????????????
        Map<String, SiteDo> siteMap = siteDos.stream().collect(Collectors.toMap(siteDo -> siteDo.getId().toString(), Function.identity(), (key1, key2) -> key2));

        // ????????????id??????
        List<Integer> siteIds = siteDos.stream().map(site -> site.getId()).distinct().collect(Collectors.toList());

        // ?????????????????????
        List<ManagerDo> managerDos = managerMapper.queryBySiteIds(siteIds);

        // ??????????????????????????? ?????????????????????
        managerDos.stream().forEach(manager -> {
            SiteDo site = siteMap.get(manager.getSiteId().toString());
            managerList.add(
                    ManagerVo.builder()
                            .province(site.getProvince())
                            .city(site.getCity())
                            .siteAddress(site.getSiteAddress())
                            .siteName(site.getSiteName())
                            .phone(manager.getPhone()).build());
        });
        return managerList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CheckPermission(UserRoleEnum.ADMIN)
    public Result distribution(ChangeDistributionRequest request) {
        //????????????
        ValidateExtUtil.validate(request);

        //???????????????????????????
        ManagerDo oldManager = managerMapper.queryByPhone(request.getPhone());
        if (ObjUtil.isEmpty(oldManager)) {
            throw new GlobalException(ResultCodeEnum.NOT_MANAGER);
        }

        // ????????????????????????
        List<SiteDo> siteDos = siteMapper.queryAllSite();
        if (!(siteDos.stream().map(v -> v.getId()).anyMatch(integer -> integer.compareTo(request.getSiteId()) == 0))) {
            throw new GlobalException(ResultCodeEnum.NOT_HAVE_SITE);
        }

        // ???????????????????????????
        ManagerDo manager = ManagerDo.builder().phone(request.getPhone()).siteId(request.getSiteId()).build();

        // ??????????????????????????????
        if (!(managerMapper.updateSiteIdByPhone(manager) > 0)) {
            throw new GlobalException(ResultCodeEnum.DISTRIBUTION_FAIL);
        }

        //??????????????????????????????
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();
        // ????????????????????????
        ManagerOperationDo managerOperationDo = ManagerOperationDo.builder().phone(request.getPhone())
                .siteId(oldManager.getSiteId())
                .newSiteId(request.getSiteId())
                .operationTime(new Date())
                .operationUser(userPhone)
                .oerationIp(httpServletRequest.getRemoteAddr()).build();

        // ??????????????????????????????
        if (!(managerOperationMapper.insertInfo(managerOperationDo) > 0)) {
            throw new GlobalException(ResultCodeEnum.CHANGE_SITE_FAIL);
        }

        return new Result(ResultCodeEnum.CHANGE_SITE_SUCCESS);
    }

    @Override
    @CheckPermission(UserRoleEnum.ADMIN)
    public Result removeManager(RemoveManagerRequest request) {
        // ????????????
        ValidateExtUtil.validate(request);

        // ??????????????????????????????
        List<ManagerDo> managerDos = managerMapper.selectAllManager();
        if (!(managerDos.stream().map(v -> v.getPhone()).anyMatch(v -> request.getPhone().equals(v)))) {
            throw new GlobalException(ResultCodeEnum.NOT_MANAGER);
        }

        // ???????????????
        if (!(managerMapper.deleteByPhone(request.getPhone()) > 0)) {
            throw new GlobalException(ResultCodeEnum.REMOVE_MANAGER_FAIL);
        }

        // ??????????????????????????????
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        String userPhone = jwtUtil.getInfo(httpServletRequest.getHeader("token")).get("username").toString();

        // ????????????????????????
        UserOperationDo info = UserOperationDo.builder().operationUser(userPhone)
                .operationIp(httpServletRequest.getRemoteAddr())
                .phone(request.getPhone())
                .operationTime(new Date())
                .operationInfo("?????????????????????").build();

        // ??????????????????????????????
        if (!(userOperationMapper.insertInfo(info) > 0)) {
            throw new GlobalException(ResultCodeEnum.REMOVE_MANAGER_FAIL);
        }

        return new Result(ResultCodeEnum.REMOVE_MANAGER_SUCCESS);
    }

    @Override
    @CheckPermission(UserRoleEnum.ADMIN)
    public Result getAllManagers(Integer index) {
        // ??????pagehelper??????
        PageHelper.startPage(index, PAGE_NUM);

        // ???????????????????????????
        List<ManagerDo> managerDos = managerMapper.selectAllManager();
        Page page = (Page) managerDos;

        // ??????????????????
        List<SiteDo> siteDos = siteMapper.queryAllSite();
        Map<String, SiteDo> siteDoMap = siteDos.stream().collect(Collectors.toMap(siteDo -> siteDo.getId().toString(), Function.identity(), (key1, key2) -> key2));

        // DO -> VO
        ArrayList<ManagerVo> managerVos = new ArrayList<>();
        managerDos.forEach(v -> {
            if (ObjUtil.isNotEmpty(v.getSiteId()) && siteDos.stream().anyMatch(site -> site.getId().equals(v.getSiteId()))) {
                SiteDo site = siteDoMap.get(v.getSiteId().toString());
                managerVos.add(
                        ManagerVo.builder()
                                .phone(v.getPhone())
                                .siteName(site.getSiteName())
                                .siteAddress(site.getSiteAddress())
                                .province(site.getProvince())
                                .city(site.getCity())
                                .build()
                );
            }
        });

        // ??????????????????
        long total = page.getTotal() - (page.getTotal() - managerVos.size());
        long pageSize = total % PAGE_NUM == 0 ? total / PAGE_NUM : total / PAGE_NUM + 1;
        PageResult result = PageResult.builder().pages((int) pageSize)
                .pageSize(PAGE_NUM)
                .pageIndex(page.getPageNum())
                .startRow(page.getStartRow())
                .endRow(page.getEndRow())
                .total(total)
                .data(managerVos).build();

        return new Result(ResultCodeEnum.QUERY_ALL_USER_SUCCESS, result);

    }
}
