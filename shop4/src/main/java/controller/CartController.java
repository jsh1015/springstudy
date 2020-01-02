package controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("cart")
public class CartController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("cartAdd")
	public ModelAndView add(String id, Integer quantity, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id); //선택된 상품객체
		Cart cart = (Cart)session.getAttribute("CART");
		int total = 0;
		if(cart == null) { //장바구니가 없으면
			cart = new Cart();//새로만들고
			session.setAttribute("CART", cart);//session에 cart를 추가
		}
		cart.push(new ItemSet(item,quantity));//itemSetlist로 추가
		mav.addObject("message",item.getName()+":"+quantity+"개 장바구니 추가");
		mav.addObject("cart",cart);
		return mav;
	}

	/*
		index 파라미터값에 해당하는 값을 Cart의 ItemSetList 객체에서 제거
		message에 000상품을 장바구니에서 제거 메세지 view(cart.jsp)에 전달
		1. session에서 CART 객체를 조회
		2. cart 객체에서 itemSetList객체에서 index에 해당하는 값을 제거
		 	ItemSet itemset = cart.getItemSetList().remove(index)
		3. message, cart를 view에 전달
	 */
	@RequestMapping("cartDelete") //장바구니에 있는 목록 삭제하기
	public ModelAndView delete(int index, HttpSession session) { 
		//index의 타입을 Integer로 하면 remove에서 객체를 만들어버려서 ItemSet으로 받아올 수 없음
		//0번지를 지우는게 아닌 현재내가가진 객체를 지우라는 의미가 되어버림 ==> booleasn으로 return
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet item = null;
		try {//index값이 없는 경우
			item = cart.getItemSetList().remove(index); //제거된 ItemSet 객체를 전달
			mav.addObject("message", item.getItem().getName()+" 상품을 장바구니에서 제거");
		}catch(Exception e) {
			mav.addObject("message", "장바구니 상품이 삭제되지 않았습니다.");
		}
		mav.addObject("cart",cart);
		return mav;
	}
	/*
		장바구니 상품이 없는 경우 CartEmptyException을 발생시킴 
		"장바구니에 상품이 없습니다." 메세지를 alert창으로 출력
		/item/list.shop 페이지 이동
	 */
	@RequestMapping("cartView")
	public ModelAndView delete(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart==null || cart.getItemSetList().size()==0) {
			throw new CartEmptyException("장바구니에 상품이 없습니다.","../item/list.shop");
			//강제예외발생
		}
		mav.addObject("cart",cart);
		return mav;
	}

	//주문전 확인
	//반드시 로그인이 필요
	//장바구니에 상품이 존재해여함
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
		return "cart/checkout";
	}
	/*
		주문 확정
		1. 세션에서(CART, loginUser 정보 조회)
		2. sale, saleitem 테이블에 데이터를 추가
		3. 장바구니에서 상품 제거
		4. cart/end.jsp 페이지로 이동
	 */
	@RequestMapping("end") //전에 CartAspect 실행
	public ModelAndView checkend(HttpSession session) { //CartAspect클래스의 대상이 됨
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart) session.getAttribute("CART");
		User loginUser = (User) session.getAttribute("loginUser");
		//sale : 주문 정보 내역
		Sale sale = service.checkend(loginUser, cart); //db에등록, 최종내용 전달.
		long total = cart.getTotal(); //현재 나의 총금액==주문상품의 총 금액 리턴(제거전에)
//		cart.clearItem();
		session.removeAttribute("CART");// 3.
		mav.addObject("sale",sale);
		mav.addObject("total",total);
		return mav;
	}
}
