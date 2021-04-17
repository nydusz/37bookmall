package sanqibookmall.service.impl;

import sanqibookmall.common.ServiceResultEnum;
import sanqibookmall.controller.vo.SanqiSearchGoodsVO;
import sanqibookmall.dao.SanqiGoodsMapper;
import sanqibookmall.entity.SanqiGoods;
import sanqibookmall.service.SanqiGoodsService;
import sanqibookmall.util.BeanUtil;
import sanqibookmall.util.PageQueryUtil;
import sanqibookmall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SanqiGoodsServiceImpl implements SanqiGoodsService {

    @Autowired
    private SanqiGoodsMapper goodsMapper;

    @Override
    public PageResult getGoodsPage(PageQueryUtil pageUtil) {
        List<SanqiGoods> goodsList = goodsMapper.findGoodsList(pageUtil);
        int total = goodsMapper.getTotalGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveGoods(SanqiGoods goods) {
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveGoods(List<SanqiGoods> sanqiGoodsList) {
        if (!CollectionUtils.isEmpty(sanqiGoodsList)) {
            goodsMapper.batchInsert(sanqiGoodsList);
        }
    }

    @Override
    public String updateGoods(SanqiGoods goods) {
        SanqiGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public SanqiGoods getGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchGoodsByMajor(PageQueryUtil pageUtil) {
        List<SanqiGoods> goodsList = goodsMapper.findGoodsListByMajor(pageUtil);
        int total = goodsMapper.getTotalGoodsByMajor(pageUtil);
        List<SanqiSearchGoodsVO> sanqiSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            sanqiSearchGoodsVOS = BeanUtil.copyList(goodsList, SanqiSearchGoodsVO.class);
            for (SanqiSearchGoodsVO sanqiSearchGoodsVO : sanqiSearchGoodsVOS) {
                String goodsName = sanqiSearchGoodsVO.getGoodsName();
                String goodsIntro = sanqiSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    sanqiSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    sanqiSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(sanqiSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public PageResult searchGoods(PageQueryUtil pageUtil) {
        List<SanqiGoods> goodsList = goodsMapper.findGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalGoodsBySearch(pageUtil);
        List<SanqiSearchGoodsVO> sanqiSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            sanqiSearchGoodsVOS = BeanUtil.copyList(goodsList, SanqiSearchGoodsVO.class);
            for (SanqiSearchGoodsVO sanqiSearchGoodsVO : sanqiSearchGoodsVOS) {
                String goodsName = sanqiSearchGoodsVO.getGoodsName();
                String goodsIntro = sanqiSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    sanqiSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    sanqiSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(sanqiSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
