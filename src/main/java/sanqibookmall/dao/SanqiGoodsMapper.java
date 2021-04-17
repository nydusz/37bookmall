package sanqibookmall.dao;

import sanqibookmall.entity.SanqiGoods;
import sanqibookmall.entity.StockNumDTO;
import sanqibookmall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SanqiGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(SanqiGoods record);

    int insertSelective(SanqiGoods record);

    SanqiGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(SanqiGoods record);

    int updateByPrimaryKeyWithBLOBs(SanqiGoods record);

    int updateByPrimaryKey(SanqiGoods record);

    List<SanqiGoods> findGoodsList(PageQueryUtil pageUtil);

    int getTotalGoods(PageQueryUtil pageUtil);

    List<SanqiGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<SanqiGoods> findGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalGoodsBySearch(PageQueryUtil pageUtil);

    List<SanqiGoods> findGoodsListByMajor(PageQueryUtil pageUtil);

    int getTotalGoodsByMajor(PageQueryUtil pageUtil);

    int batchInsert(@Param("sanqiGoodsList") List<SanqiGoods> sanqiGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}