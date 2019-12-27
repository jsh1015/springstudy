package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

@Controller //@Component + Controller : 객체를 만들고 컨트롤러의 기능까지 같이 수행
@RequestMapping("item") //item/xxx.shop
public class ItemController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list") //item/list.shop 요청
	public ModelAndView list(){
		//itemList : item 테이블의 모든 레코드와 모든 컬럼의 정보를 저장
		List<Item> itemList = service.getItemList();
		ModelAndView mav = new ModelAndView(); //뷰 : item/list
		mav.addObject("itemList",itemList);
		return mav; //  /WEB-INF/view/item/list.jsp
	}
	
	@RequestMapping("create") //item/create.shop 요청
	public ModelAndView create(){
		ModelAndView mav = new ModelAndView("item/add"); //add.jsp 로감
		mav.addObject(new Item());
		return mav; //  /WEB-INF/view/item/create.jsp
	}
	
	@RequestMapping("register") //@Valid: 유효성 검증을 하라는 것
	public ModelAndView register(@Valid Item item, BindingResult bresult, HttpServletRequest request) { 
		ModelAndView mav = new ModelAndView("item/add");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemCreate(item,request); //itemCreate : item 등록, 파일도 등록
		mav.setViewName("redirect:/item/list.shop"); //정상적으로 처리되면 (redirect) 재요청하게함
		return mav;
	}
	
	@PostMapping("update")
	public ModelAndView update(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/edit");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemUpdate(item,request);
		mav.setViewName("redirect:/item/list.shop"); //업데이트가 되면 redirect
		return mav;
	}
	
//	@RequestMapping("detail")
	@GetMapping("*") //그외 GET 방식 요청시 호출되는 메서드. detail 등 해당 주소가 없을때 *를 찾아서 들어옴
	public ModelAndView itemSelect(String id){ //id값이없으면 null로 들어옴
		Item item = service.getItem(id);
		ModelAndView mav = new ModelAndView(); //add.jsp 로감
		mav.addObject("item",item);
		return mav;
	}
	
	@GetMapping("delete")
	public ModelAndView itemdelete(int id) {
		ModelAndView mav = new ModelAndView();
		service.itemDelete(id);
		mav.setViewName("redirect:/item/list.shop"); //업데이트가 되면 redirect
		return mav;
	}
}
