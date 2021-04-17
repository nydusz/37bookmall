package sanqibookmall.service;

import sanqibookmall.controller.vo.SanqiOrderDetailVO;
import sanqibookmall.controller.vo.SanqiOrderItemVO;
import sanqibookmall.controller.vo.SanqiShoppingCartItemVO;
import sanqibookmall.controller.vo.SanqiUserVO;
import sanqibookmall.entity.SanqiOrder;
import sanqibookmall.util.PageQueryUtil;
import sanqibookmall.util.PageResult;

import java.util.List;

public interface SanqiOrderService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param sanqiOrder
     * @return
     */
    String updateOrderInfo(SanqiOrder sanqiOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(SanqiUserVO user, List<SanqiShoppingCartItemVO> myShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    SanqiOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    SanqiOrder getOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<SanqiOrderItemVO> getOrderItems(Long id);
}
