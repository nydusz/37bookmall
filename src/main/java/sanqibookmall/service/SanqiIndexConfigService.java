package sanqibookmall.service;

import sanqibookmall.controller.vo.SanqiIndexConfigGoodsVO;
import sanqibookmall.entity.IndexConfig;
import sanqibookmall.util.PageQueryUtil;
import sanqibookmall.util.PageResult;

import java.util.List;

public interface SanqiIndexConfigService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<SanqiIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    List<SanqiIndexConfigGoodsVO> getRecommendGoodsesForIndex(int configType, int number,String major);

    Boolean deleteBatch(Long[] ids);
}
