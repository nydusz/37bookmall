package sanqibookmall.controller.mall;

import sanqibookmall.common.Constants;
import sanqibookmall.common.ServiceResultEnum;
import sanqibookmall.controller.vo.SanqiShoppingCartItemVO;
import sanqibookmall.controller.vo.SanqiUserVO;
import sanqibookmall.entity.SanqiShoppingCartItem;
import sanqibookmall.service.SanqiShoppingCartService;
import sanqibookmall.util.Result;
import sanqibookmall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    private SanqiShoppingCartService sanqiShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        SanqiUserVO user = (SanqiUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<SanqiShoppingCartItemVO> myShoppingCartItems = sanqiShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(SanqiShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/error_5xx";
            }
            //总价
            for (SanqiShoppingCartItemVO sanqiShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += sanqiShoppingCartItemVO.getGoodsCount() * sanqiShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveShoppingCartItem(@RequestBody SanqiShoppingCartItem sanqiShoppingCartItem,
                                       HttpSession httpSession) {
        SanqiUserVO user = (SanqiUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        sanqiShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = sanqiShoppingCartService.saveCartItem(sanqiShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateShoppingCartItem(@RequestBody SanqiShoppingCartItem sanqiShoppingCartItem,
                                         HttpSession httpSession) {
        SanqiUserVO user = (SanqiUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        sanqiShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String updateResult = sanqiShoppingCartService.updateCartItem(sanqiShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{shoppingCartItemId}")
    @ResponseBody
    public Result updateShoppingCartItem(@PathVariable("shoppingCartItemId") Long shoppingCartItemId,
                                         HttpSession httpSession) {
        SanqiUserVO user = (SanqiUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = sanqiShoppingCartService.deleteById(shoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        SanqiUserVO user = (SanqiUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<SanqiShoppingCartItemVO> myShoppingCartItems = sanqiShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (SanqiShoppingCartItemVO sanqiShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += sanqiShoppingCartItemVO.getGoodsCount() * sanqiShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }
}
