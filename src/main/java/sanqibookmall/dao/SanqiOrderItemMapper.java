package sanqibookmall.dao;

import sanqibookmall.entity.SanqiOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SanqiOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(SanqiOrderItem record);

    int insertSelective(SanqiOrderItem record);

    SanqiOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<SanqiOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<SanqiOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<SanqiOrderItem> orderItems);

    int updateByPrimaryKeySelective(SanqiOrderItem record);

    int updateByPrimaryKey(SanqiOrderItem record);
}