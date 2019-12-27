package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

//설정에 의해 index.shop 요청시 호출되는 클래스
public class DetailController {
	private ShopService shopService;
	
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	
	@RequestMapping //detail.shop?id=1
	public ModelAndView detail(Integer id) { //파라미터와 매개변수 명을 같게 하면 알아서 다해줌
		Item item = shopService.getItemById(id);
		ModelAndView mav = new ModelAndView(); //'detail'
		mav.addObject("item",item);
		return mav;
	}
}