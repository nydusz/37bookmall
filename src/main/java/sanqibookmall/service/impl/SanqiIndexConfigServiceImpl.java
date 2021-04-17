package sanqibookmall.service.impl;

import sanqibookmall.common.ServiceResultEnum;
import sanqibookmall.controller.vo.SanqiIndexConfigGoodsVO;
import sanqibookmall.dao.IndexConfigMapper;
import sanqibookmall.dao.SanqiGoodsMapper;
import sanqibookmall.entity.IndexConfig;
import sanqibookmall.entity.SanqiGoods;
import sanqibookmall.service.SanqiIndexConfigService;
import sanqibookmall.util.BeanUtil;
import sanqibookmall.util.PageQueryUtil;
import sanqibookmall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SanqiIndexConfigServiceImpl implements SanqiIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private SanqiGoodsMapper goodsMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<SanqiIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<SanqiIndexConfigGoodsVO> sanqiIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<SanqiGoods> sanqiGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            sanqiIndexConfigGoodsVOS = BeanUtil.copyList(sanqiGoods, SanqiIndexConfigGoodsVO.class);
            for (SanqiIndexConfigGoodsVO sanqiIndexConfigGoodsVO : sanqiIndexConfigGoodsVOS) {
                String goodsName = sanqiIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = sanqiIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    sanqiIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    sanqiIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return sanqiIndexConfigGoodsVOS;
    }

    @Override
    public List<SanqiIndexConfigGoodsVO> getRecommendGoodsesForIndex(int configType, int number,String major) {
        List<SanqiIndexConfigGoodsVO> sanqiIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findRecommendConfigsByTypeAndNum(configType, number,major);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<SanqiGoods> sanqiGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            sanqiIndexConfigGoodsVOS = BeanUtil.copyList(sanqiGoods, SanqiIndexConfigGoodsVO.class);
            for (SanqiIndexConfigGoodsVO sanqiIndexConfigGoodsVO : sanqiIndexConfigGoodsVOS) {
                String goodsName = sanqiIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = sanqiIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    sanqiIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    sanqiIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return sanqiIndexConfigGoodsVOS;
    }


    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}
