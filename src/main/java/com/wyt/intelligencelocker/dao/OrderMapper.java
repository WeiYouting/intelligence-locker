package com.wyt.intelligencelocker.dao;

import com.wyt.intelligencelocker.entity.Do.OrderDo;
import com.wyt.intelligencelocker.meta.enums.OrderStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Author WeiYouting
 * @create 2022/10/9 9:20
 * @Email Wei.youting@qq.com
 */
@Repository
@Mapper
public interface OrderMapper {

    Integer insertOrder(OrderDo order);

    List<OrderDo> queryAllOrder();

    List<OrderDo> queryByGenerationTime(@Param("start") Date start, @Param("end") Date end);

    OrderDo queryByOrderId(@Param("id") String orderId);

    List<OrderDo> queryBySiteIds(@Param("idList") List<Integer> idList);

    List<OrderDo> queryByRider(@Param("tel") String rider);

    List<OrderDo> queryByCustomer(@Param("tel") String customer);

    List<OrderDo> queryByStatus(@Param("status") Integer status);

    List<OrderDo> queryBySiteIdByStatus(@Param("status") Integer status,@Param("idList") List<Integer> ids);

    List<OrderDo> queryByPhone(@Param("tel") String userPhone);

    List<OrderDo> queryByPhoneByNotStatus(@Param("status") Integer status,@Param("tel") String userPhone);

    Integer updateStatus(@Param("orderId") String orderId, @Param("status") Integer status,@Param("completionTime") Date completionTime,@Param("lockerId") Integer lockerId,@Param("rider") String rider);

    Integer countByOrderIdInteger(@Param("id") String order);
}
