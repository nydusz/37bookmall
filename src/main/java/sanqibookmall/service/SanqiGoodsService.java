package sanqibookmall.service;

import sanqibookmall.entity.SanqiGoods;
import sanqibookmall.util.PageQueryUtil;
import sanqibookmall.util.PageResult;

import java.util.List;

public interface SanqiGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveGoods(SanqiGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param sanqiGoodsList
     * @return
     */
    void batchSaveGoods(List<SanqiGoods> sanqiGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateGoods(SanqiGoods goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    SanqiGoods getGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchGoods(PageQueryUtil pageUtil);

    PageResult searchGoodsByMajor(PageQueryUtil pageUtil);
}
