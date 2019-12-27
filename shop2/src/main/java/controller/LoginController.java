package controller;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

public class LoginController {
	private ShopService shopService;
	private Validator validator;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	@GetMapping //4.0 이후버전에서 가능
	//@RequestMapping(method=RequestMethod.GET)
	public String loginForm(Model model) { //Model뷰에 데이터를 전달할 객체
		model.addAttribute(new User());
		return "login";
	}
	@PostMapping //	post방식											세션객체 
	public ModelAndView login(User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		validator.validate(user, bresult);
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			User dbuser = shopService.getUser(user.getUserid()); //dbuser null이면 예외 발생
			if(user.getPassword().equals(dbuser.getPassword())) { //dbuser가 있으면 비밀번호 검증
				session.setAttribute("loginUser", dbuser);//session에 해당 회원의 모든 정보가 들어있음
			}else {
				//비밀번호가 틀린경우
				bresult.reject("error.login.password");//(비밀번호를 확인하세요.)
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
		}catch(EmptyResultDataAccessException e) {
			//EmptyResultDataAccessException : db에서 조회된 레코드가 없는경우
			bresult.reject("error.login.id"); //(아이디를 확인하세요.)
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.setViewName("loginSuccess");
		return mav;
	}
}
