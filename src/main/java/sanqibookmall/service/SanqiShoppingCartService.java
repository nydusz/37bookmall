package sanqibookmall.service;

import sanqibookmall.controller.vo.SanqiShoppingCartItemVO;
import sanqibookmall.entity.SanqiShoppingCartItem;

import java.util.List;

public interface SanqiShoppingCartService {

    /**
     * 保存商品至购物车中
     */
    String saveCartItem(SanqiShoppingCartItem sanqiShoppingCartItem);

    /**
     * 修改购物车中的属性
     */
    String updateCartItem(SanqiShoppingCartItem sanqiShoppingCartItem);

    /**
     * 获取购物项详情
     */
    SanqiShoppingCartItem getCartItemById(Long shoppingCartItemId);

    /**
     * 删除购物车中的商品
     */
    Boolean deleteById(Long shoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     */
    List<SanqiShoppingCartItemVO> getMyShoppingCartItems(Long sanqiUserId);
}
