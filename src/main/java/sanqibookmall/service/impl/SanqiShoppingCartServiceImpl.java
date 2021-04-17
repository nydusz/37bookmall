package sanqibookmall.service.impl;

import sanqibookmall.common.Constants;
import sanqibookmall.common.ServiceResultEnum;
import sanqibookmall.controller.vo.SanqiShoppingCartItemVO;
import sanqibookmall.dao.SanqiGoodsMapper;
import sanqibookmall.dao.SanqiShoppingCartItemMapper;
import sanqibookmall.entity.SanqiGoods;
import sanqibookmall.entity.SanqiShoppingCartItem;
import sanqibookmall.service.SanqiShoppingCartService;
import sanqibookmall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SanqiShoppingCartServiceImpl implements SanqiShoppingCartService {

    @Autowired
    private SanqiShoppingCartItemMapper sanqiShoppingCartItemMapper;

    @Autowired
    private SanqiGoodsMapper sanqiGoodsMapper;

    //todo 修改session中购物项数量

    @Override
    public String saveCartItem(SanqiShoppingCartItem sanqiShoppingCartItem) {
        SanqiShoppingCartItem temp = sanqiShoppingCartItemMapper.selectByUserIdAndGoodsId(sanqiShoppingCartItem.getUserId(), sanqiShoppingCartItem.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            //todo count = tempCount + 1
            temp.setGoodsCount(sanqiShoppingCartItem.getGoodsCount());
            return updateCartItem(temp);
        }
        SanqiGoods sanqiGoods = sanqiGoodsMapper.selectByPrimaryKey(sanqiShoppingCartItem.getGoodsId());
        //商品为空
        if (sanqiGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = sanqiShoppingCartItemMapper.selectCountByUserId(sanqiShoppingCartItem.getUserId()) + 1;
        //超出单个商品的最大数量
        if (sanqiShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (sanqiShoppingCartItemMapper.insertSelective(sanqiShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCartItem(SanqiShoppingCartItem sanqiShoppingCartItem) {
        SanqiShoppingCartItem sanqiShoppingCartItemUpdate = sanqiShoppingCartItemMapper.selectByPrimaryKey(sanqiShoppingCartItem.getCartItemId());
        if (sanqiShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出单个商品的最大数量
        if (sanqiShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        //todo userId不同不能修改
        sanqiShoppingCartItemUpdate.setGoodsCount(sanqiShoppingCartItem.getGoodsCount());
        sanqiShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (sanqiShoppingCartItemMapper.updateByPrimaryKeySelective(sanqiShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public SanqiShoppingCartItem getCartItemById(Long shoppingCartItemId) {
        return sanqiShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long shoppingCartItemId) {
        //todo userId不同不能删除
        return sanqiShoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId) > 0;
    }

    @Override
    public List<SanqiShoppingCartItemVO> getMyShoppingCartItems(Long sanqiUserId) {
        List<SanqiShoppingCartItemVO> sanqiShoppingCartItemVOS = new ArrayList<>();
        List<SanqiShoppingCartItem> sanqiShoppingCartItems = sanqiShoppingCartItemMapper.selectByUserId(sanqiUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(sanqiShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> goodsIds = sanqiShoppingCartItems.stream().map(SanqiShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<SanqiGoods> sanqiGoods = sanqiGoodsMapper.selectByPrimaryKeys(goodsIds);
            Map<Long, SanqiGoods> goodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(sanqiGoods)) {
                goodsMap = sanqiGoods.stream().collect(Collectors.toMap(SanqiGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (SanqiShoppingCartItem sanqiShoppingCartItem : sanqiShoppingCartItems) {
                SanqiShoppingCartItemVO sanqiShoppingCartItemVO = new SanqiShoppingCartItemVO();
                BeanUtil.copyProperties(sanqiShoppingCartItem, sanqiShoppingCartItemVO);
                if (goodsMap.containsKey(sanqiShoppingCartItem.getGoodsId())) {
                    SanqiGoods sanqiGoodsTemp = goodsMap.get(sanqiShoppingCartItem.getGoodsId());
                    sanqiShoppingCartItemVO.setGoodsCoverImg(sanqiGoodsTemp.getGoodsCoverImg());
                    String goodsName = sanqiGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    sanqiShoppingCartItemVO.setGoodsName(goodsName);
                    sanqiShoppingCartItemVO.setSellingPrice(sanqiGoodsTemp.getSellingPrice());
                    sanqiShoppingCartItemVOS.add(sanqiShoppingCartItemVO);
                }
            }
        }
        return sanqiShoppingCartItemVOS;
    }
}
