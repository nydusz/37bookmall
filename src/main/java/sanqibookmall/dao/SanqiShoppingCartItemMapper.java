package sanqibookmall.dao;

import sanqibookmall.entity.SanqiShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SanqiShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(SanqiShoppingCartItem record);

    int insertSelective(SanqiShoppingCartItem record);

    SanqiShoppingCartItem selectByPrimaryKey(Long cartItemId);

    SanqiShoppingCartItem selectByUserIdAndGoodsId(@Param("sanqiUserId") Long sanqiUserId, @Param("goodsId") Long goodsId);

    List<SanqiShoppingCartItem> selectByUserId(@Param("sanqiUserId") Long sanqiUserId, @Param("number") int number);

    int selectCountByUserId(Long sanqiUserId);

    int updateByPrimaryKeySelective(SanqiShoppingCartItem record);

    int updateByPrimaryKey(SanqiShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}