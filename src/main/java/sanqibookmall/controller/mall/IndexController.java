package sanqibookmall.controller.mall;

import sanqibookmall.common.Constants;
import sanqibookmall.common.IndexConfigTypeEnum;
import sanqibookmall.controller.vo.SanqiIndexCarouselVO;
import sanqibookmall.controller.vo.SanqiIndexCategoryVO;
import sanqibookmall.controller.vo.SanqiIndexConfigGoodsVO;
import sanqibookmall.service.SanqiCarouselService;
import sanqibookmall.service.SanqiCategoryService;
import sanqibookmall.service.SanqiIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private SanqiCarouselService sanqiCarouselService;

    @Resource
    private SanqiIndexConfigService sanqiIndexConfigService;

    @Resource
    private SanqiCategoryService sanqiCategoryService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<SanqiIndexCategoryVO> categories = sanqiCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<SanqiIndexCarouselVO> carousels = sanqiCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<SanqiIndexConfigGoodsVO> hotGoodses = sanqiIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<SanqiIndexConfigGoodsVO> newGoodses = sanqiIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<SanqiIndexConfigGoodsVO> recommendGoodses = sanqiIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "mall/index";
    }
}
