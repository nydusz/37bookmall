package sanqibookmall.controller.mall;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import sanqibookmall.common.Constants;
import sanqibookmall.common.IndexConfigTypeEnum;
import sanqibookmall.common.bookException;
import sanqibookmall.common.ServiceResultEnum;
import sanqibookmall.controller.vo.SanqiGoodsDetailVO;
import sanqibookmall.controller.vo.SanqiIndexConfigGoodsVO;
import sanqibookmall.controller.vo.SanqiUserVO;
import sanqibookmall.controller.vo.SearchPageCategoryVO;
import sanqibookmall.entity.SanqiGoods;
import sanqibookmall.service.SanqiCategoryService;
import sanqibookmall.service.SanqiGoodsService;
import sanqibookmall.service.SanqiIndexConfigService;
import sanqibookmall.service.SanqiUserService;
import sanqibookmall.util.BeanUtil;
import sanqibookmall.util.PageQueryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Controller
public class GoodsController {

    @Resource
    private SanqiGoodsService sanqiGoodsService;
    @Resource
    private SanqiCategoryService sanqiCategoryService;
    @Resource
    private SanqiIndexConfigService sanqiIndexConfigService;

    @GetMapping({"/search", "/search.html"})
    public String searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //封装分类数据
        if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")) {
            Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
            SearchPageCategoryVO searchPageCategoryVO = sanqiCategoryService.getCategoriesForSearch(categoryId);
            if (searchPageCategoryVO != null) {
                request.setAttribute("goodsCategoryId", categoryId);
                request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
            }
        }
        //封装参数供前端回显
        if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
            request.setAttribute("orderBy", params.get("orderBy") + "");
        }
        String keyword = "";
        //对keyword做过滤 去掉空格
        if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
            keyword = params.get("keyword") + "";
        }
        request.setAttribute("keyword", keyword);
        params.put("keyword", keyword);
        //搜索上架状态下的商品
        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
        //封装商品数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("pageResult", sanqiGoodsService.searchGoods(pageUtil));
        return "mall/search";
    }

    @GetMapping("/recommend")
    public String recommendGoods(@RequestParam Map<String, Object> params,HttpServletRequest request, HttpSession httpSession) {

        SanqiUserVO user = (SanqiUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String major= user.getMajor();
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //封装分类数据
        if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")) {
            Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
            SearchPageCategoryVO searchPageCategoryVO = sanqiCategoryService.getCategoriesForSearch(categoryId);
            if (searchPageCategoryVO != null) {
                request.setAttribute("goodsCategoryId", categoryId);
                request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
            }
        }
        //封装参数供前端回显
        if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
            request.setAttribute("orderBy", params.get("orderBy") + "");
        }
        String keyword = major;
        //对keyword做过滤 去掉空格
        if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
            keyword = params.get("keyword") + "";
        }
        request.setAttribute("keyword", keyword);
        params.put("keyword", keyword);
        //搜索上架状态下的商品
        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
        //封装商品数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("pageResult", sanqiGoodsService.searchGoodsByMajor(pageUtil));
        return "mall/recommend";
    }



    @GetMapping("/goods/detail/{goodsId}")
    public String detailPage(@PathVariable("goodsId") Long goodsId,
                             HttpSession httpSession
                             ,HttpServletRequest request) {
        if (goodsId < 1) {
            return "error/error_5xx";
        }
        SanqiUserVO user = (SanqiUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String major= user.getMajor();
        System.out.println(major);
        SanqiGoods goods = sanqiGoodsService.getGoodsById(goodsId);
        if (goods == null) {
            bookException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        if (Constants.SELL_STATUS_UP != goods.getGoodsSellStatus()) {
            bookException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
        }
        SanqiGoodsDetailVO goodsDetailVO = new SanqiGoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        List<SanqiIndexConfigGoodsVO> recommend = sanqiIndexConfigService.getRecommendGoodsesForIndex(6,4,major);
        request.setAttribute("goodsDetail", goodsDetailVO);
        request.setAttribute("recommend", recommend);//推荐商品
        return "mall/detail";
    }

}
