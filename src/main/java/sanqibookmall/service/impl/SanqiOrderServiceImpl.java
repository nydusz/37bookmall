package sanqibookmall.service.impl;

import sanqibookmall.common.*;
import sanqibookmall.common.bookException;
import sanqibookmall.controller.vo.*;
import sanqibookmall.dao.SanqiGoodsMapper;
import sanqibookmall.dao.SanqiOrderItemMapper;
import sanqibookmall.dao.SanqiOrderMapper;
import sanqibookmall.dao.SanqiShoppingCartItemMapper;
import sanqibookmall.entity.SanqiGoods;
import sanqibookmall.entity.SanqiOrder;
import sanqibookmall.entity.SanqiOrderItem;
import sanqibookmall.entity.StockNumDTO;
import sanqibookmall.service.SanqiOrderService;
import sanqibookmall.util.BeanUtil;
import sanqibookmall.util.NumberUtil;
import sanqibookmall.util.PageQueryUtil;
import sanqibookmall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class SanqiOrderServiceImpl implements SanqiOrderService {

    @Autowired
    private SanqiOrderMapper sanqiOrderMapper;
    @Autowired
    private SanqiOrderItemMapper sanqiOrderItemMapper;
    @Autowired
    private SanqiShoppingCartItemMapper sanqiShoppingCartItemMapper;
    @Autowired
    private SanqiGoodsMapper sanqiGoodsMapper;

    @Override
    public PageResult getOrdersPage(PageQueryUtil pageUtil) {
        List<SanqiOrder> sanqiOrders = sanqiOrderMapper.findOrderList(pageUtil);
        int total = sanqiOrderMapper.getTotalOrders(pageUtil);
        PageResult pageResult = new PageResult(sanqiOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(SanqiOrder sanqiOrder) {
        SanqiOrder temp = sanqiOrderMapper.selectByPrimaryKey(sanqiOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(sanqiOrder.getTotalPrice());
            temp.setUserAddress(sanqiOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (sanqiOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SanqiOrder> orders = sanqiOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SanqiOrder sanqiOrder : orders) {
                if (sanqiOrder.getIsDeleted() == 1) {
                    errorOrderNos += sanqiOrder.getOrderNo() + " ";
                    continue;
                }
                if (sanqiOrder.getOrderStatus() != 1) {
                    errorOrderNos += sanqiOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (sanqiOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SanqiOrder> orders = sanqiOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SanqiOrder sanqiOrder : orders) {
                if (sanqiOrder.getIsDeleted() == 1) {
                    errorOrderNos += sanqiOrder.getOrderNo() + " ";
                    continue;
                }
                if (sanqiOrder.getOrderStatus() != 1 && sanqiOrder.getOrderStatus() != 2) {
                    errorOrderNos += sanqiOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (sanqiOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SanqiOrder> orders = sanqiOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SanqiOrder sanqiOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (sanqiOrder.getIsDeleted() == 1) {
                    errorOrderNos += sanqiOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (sanqiOrder.getOrderStatus() == 4 || sanqiOrder.getOrderStatus() < 0) {
                    errorOrderNos += sanqiOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (sanqiOrderMapper.closeOrder(Arrays.asList(ids), OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(SanqiUserVO user, List<SanqiShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(SanqiShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(SanqiShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<SanqiGoods> sanqiGoods = sanqiGoodsMapper.selectByPrimaryKeys(goodsIds);
        //检查是否包含已下架商品
        List<SanqiGoods> goodsListNotSelling = sanqiGoods.stream()
                .filter(goodsTemp -> goodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            bookException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, SanqiGoods> goodsMap = sanqiGoods.stream().collect(Collectors.toMap(SanqiGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (SanqiShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!goodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                bookException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > goodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                bookException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(sanqiGoods)) {
            if (sanqiShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = sanqiGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    bookException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                SanqiOrder sanqiOrder = new SanqiOrder();
                sanqiOrder.setOrderNo(orderNo);
                sanqiOrder.setUserId(user.getUserId());
                sanqiOrder.setUserAddress(user.getAddress());
                //总价
                for (SanqiShoppingCartItemVO sanqiShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += sanqiShoppingCartItemVO.getGoodsCount() * sanqiShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    bookException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                sanqiOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                sanqiOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (sanqiOrderMapper.insertSelective(sanqiOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<SanqiOrderItem> sanqiOrderItems = new ArrayList<>();
                    for (SanqiShoppingCartItemVO sanqiShoppingCartItemVO : myShoppingCartItems) {
                        SanqiOrderItem sanqiOrderItem = new SanqiOrderItem();
                        //使用BeanUtil工具类将sanqiShoppingCartItemVO中的属性复制到sanqiOrderItem对象中
                        BeanUtil.copyProperties(sanqiShoppingCartItemVO, sanqiOrderItem);
                        //sanqiOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        sanqiOrderItem.setOrderId(sanqiOrder.getOrderId());
                        sanqiOrderItems.add(sanqiOrderItem);
                    }
                    //保存至数据库
                    if (sanqiOrderItemMapper.insertBatch(sanqiOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    bookException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                bookException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            bookException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        bookException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public SanqiOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        SanqiOrder sanqiOrder = sanqiOrderMapper.selectByOrderNo(orderNo);
        if (sanqiOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            List<SanqiOrderItem> orderItems = sanqiOrderItemMapper.selectByOrderId(sanqiOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<SanqiOrderItemVO> sanqiOrderItemVOS = BeanUtil.copyList(orderItems, SanqiOrderItemVO.class);
                SanqiOrderDetailVO sanqiOrderDetailVO = new SanqiOrderDetailVO();
                BeanUtil.copyProperties(sanqiOrder, sanqiOrderDetailVO);
                sanqiOrderDetailVO.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(sanqiOrderDetailVO.getOrderStatus()).getName());
                sanqiOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(sanqiOrderDetailVO.getPayType()).getName());
                sanqiOrderDetailVO.setSanqiOrderItemVOS(sanqiOrderItemVOS);
                return sanqiOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public SanqiOrder getOrderByOrderNo(String orderNo) {
        return sanqiOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = sanqiOrderMapper.getTotalOrders(pageUtil);
        List<SanqiOrder> sanqiOrders = sanqiOrderMapper.findOrderList(pageUtil);
        List<SanqiOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(sanqiOrders, SanqiOrderListVO.class);
            //设置订单状态中文显示值
            for (SanqiOrderListVO sanqiOrderListVO : orderListVOS) {
                sanqiOrderListVO.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(sanqiOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = sanqiOrders.stream().map(SanqiOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<SanqiOrderItem> orderItems = sanqiOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<SanqiOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(SanqiOrderItem::getOrderId));
                for (SanqiOrderListVO sanqiOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(sanqiOrderListVO.getOrderId())) {
                        List<SanqiOrderItem> orderItemListTemp = itemByOrderIdMap.get(sanqiOrderListVO.getOrderId());
                        //将sanqiOrderItem对象列表转换成sanqiOrderItemVO对象列表
                        List<SanqiOrderItemVO> sanqiOrderItemVOS = BeanUtil.copyList(orderItemListTemp, SanqiOrderItemVO.class);
                        sanqiOrderListVO.setSanqiOrderItemVOS(sanqiOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        SanqiOrder sanqiOrder = sanqiOrderMapper.selectByOrderNo(orderNo);
        if (sanqiOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (sanqiOrderMapper.closeOrder(Collections.singletonList(sanqiOrder.getOrderId()), OrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        SanqiOrder sanqiOrder = sanqiOrderMapper.selectByOrderNo(orderNo);
        if (sanqiOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            sanqiOrder.setOrderStatus((byte) OrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            sanqiOrder.setUpdateTime(new Date());
            if (sanqiOrderMapper.updateByPrimaryKeySelective(sanqiOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        SanqiOrder sanqiOrder = sanqiOrderMapper.selectByOrderNo(orderNo);
        if (sanqiOrder != null) {
            //todo 订单状态判断 非待支付状态下不进行修改操作
            sanqiOrder.setOrderStatus((byte) OrderStatusEnum.OREDER_PAID.getOrderStatus());
            sanqiOrder.setPayType((byte) payType);
            sanqiOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            sanqiOrder.setPayTime(new Date());
            sanqiOrder.setUpdateTime(new Date());
            if (sanqiOrderMapper.updateByPrimaryKeySelective(sanqiOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<SanqiOrderItemVO> getOrderItems(Long id) {
        SanqiOrder sanqiOrder = sanqiOrderMapper.selectByPrimaryKey(id);
        if (sanqiOrder != null) {
            List<SanqiOrderItem> orderItems = sanqiOrderItemMapper.selectByOrderId(sanqiOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<SanqiOrderItemVO> sanqiOrderItemVOS = BeanUtil.copyList(orderItems, SanqiOrderItemVO.class);
                return sanqiOrderItemVOS;
            }
        }
        return null;
    }
}