package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;
import util.UserValidator;

public class UserEntryController {
	private ShopService shopService;
	private UserValidator userValidator;
	
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}
	@ModelAttribute //form에서 ModelAttribute가 보내는 값을 받음
	public User getUser() {
		User user = new User();
		user.setUsername("홍길동");
		return user;
	}
	@RequestMapping(method=RequestMethod.GET) //GET방식일때 
	public String userEntryForm() {
//		return "userEntry"; //view만 설정
		return null; //화면에 보여준것
	}
	
//	@RequestMapping(method=RequestMethod.POST) 
//	public String userEntry() {
//		return "userEntry";
//	} ==>변경
	
	@RequestMapping(method=RequestMethod.POST) 
	//user : 파라미터값(입력된 값)을 저장하고 있는 객체
	//bindResult
	public ModelAndView userEntry(User user,BindingResult bindResult) {
		ModelAndView mav = new ModelAndView();
		userValidator.validate(user, bindResult);
		if(bindResult.hasErrors()) {//에러가 존재하면
			mav.getModel().putAll(bindResult.getModel());
			return mav; //userantry로
		}
		try {
			shopService.insertUser(user);
			mav.addObject("user",user);//입력된 파라미터값을 저장하고 있는 객체
	//DataIntegrityViolationException : spring jdbc에서 발생되는 예외 == 키중복 오류
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bindResult.reject("error.duplicate.user");
			mav.getModel().putAll(bindResult.getModel());
			return mav;
		}
		mav.setViewName("userEntrySuccess"); //등록이 된경우
		return mav;
	}
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		/*
			Date.class : 형변환 대상이 되는 자료형 : User.java와 자료형이 같아야함
			format : 형식지정 yyyy-MM-dd
			true/false : 비입력을 허용/비입력 불허
		*/
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,false));
	}
}