package sanqibookmall.service;

import sanqibookmall.controller.vo.SanqiIndexCarouselVO;
import sanqibookmall.entity.Carousel;
import sanqibookmall.util.PageQueryUtil;
import sanqibookmall.util.PageResult;

import java.util.List;

public interface SanqiCarouselService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<SanqiIndexCarouselVO> getCarouselsForIndex(int number);
}
