package sanqibookmall.dao;

import sanqibookmall.entity.SanqiOrder;
import sanqibookmall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SanqiOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(SanqiOrder record);

    int insertSelective(SanqiOrder record);

    SanqiOrder selectByPrimaryKey(Long orderId);

    SanqiOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(SanqiOrder record);

    int updateByPrimaryKey(SanqiOrder record);

    List<SanqiOrder> findOrderList(PageQueryUtil pageUtil);

    int getTotalOrders(PageQueryUtil pageUtil);

    List<SanqiOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}