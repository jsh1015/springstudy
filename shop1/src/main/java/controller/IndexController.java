package controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

//설정에 의해 index.shop 요청시 호출되는 클래스
public class IndexController {
	private ShopService shopService;
	
	//itemDao 객체를 저장하고 있는 ShopService 객체 주입
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	
	@RequestMapping //index.shop 요청시 호출되는 메서드
	public ModelAndView itemList() { 
		//ModelAndView : ActionForward역할  Model : view에 전달될 data를 저장하는 객체
		//								  View  : view 설정 객체
		//itemList : item 테이블의 모든 컬럼, 모든 레코드 정보를 Item 객체의 List 객체로 저장
		List<Item> itemList = shopService.getItemList(); //데이터베이스로부터 읽어옴
		ModelAndView mav = new ModelAndView("index");//(index) : view를 설정한 것
		mav.addObject("itemList",itemList); //데이터 설정
		return mav;//view의 이름과 view에 전달될 데이터를 가지고 있는 모든 정보를 가지고있음
		// ==> DispatcherServlet이 받음
	}
}
