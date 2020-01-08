package controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Item;
import logic.Sale;
import logic.SaleItem;
import logic.ShopService;
import logic.User;
import util.CipherUtil;

@Controller //@Component + Controller : 객체를 만들고 컨트롤러의 기능까지 같이 수행
@RequestMapping("user") //user/xxx.shop
public class UserController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public String form(Model model) {
		model.addAttribute(new User());
		return null;
	}
	@PostMapping("userEntry")
	public ModelAndView userEntry(@Valid User user,BindingResult bresult) throws Exception {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//useraccount 테이블에 내용 등록. 뷰단은 login.jsp로 이동
		try {
			//암호화 부분 추가
			String key = CipherUtil.makehash(user.getUserid()).substring(0,16);
			String email = CipherUtil.encrypt(user.getEmail(),key); //이메일암호화
			user.setEmail(email);
			user.setPassword(CipherUtil.makehash(user.getPassword()));
			service.userInsert(user);
			mav.setViewName("user/login"); //redirect 를 사용하면 아이디값이 들어가지 않음
			mav.addObject("user",user);//값도 넘어감
		}catch(DataIntegrityViolationException e) {
			//e.printStackTrace();
			bresult.reject("error.duplicate.user");
		}
		return mav;
	}
	@PostMapping("login")
	public ModelAndView login(@Valid User user,BindingResult bresult,HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.login.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			User dbUser = service.getUser(user.getUserid());
			String passwd = CipherUtil.makehash(user.getPassword());
			if(!dbUser.getPassword().equals(passwd)) {
				bresult.reject("error.login.password");
				return mav;
			}else {
				try{
					String key = CipherUtil.makehash(user.getUserid()).substring(0,16);
					String email = CipherUtil.decrypt(dbUser.getEmail(),key); //이메일복호화
					dbUser.setEmail(email);
				}catch(Exception e) {
					e.printStackTrace();
				}
				session.setAttribute("loginUser",dbUser);
				mav.setViewName("redirect:main.shop");
			}
		}catch(LoginException e) {
			e.printStackTrace();
			bresult.reject("error.login.id");
		}
		return mav;
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login.shop";
	}
	
	@RequestMapping("main") //UserLoginAspect 클래스에 해당하는 핵심로직
	public String checkmain(HttpSession session) { //session을 받지 않으면 로그인안한사람도 접근가능
		return "user/main";
	}
	
	//로그인 검증, (로그인 정보 != 파라미터정보 접근 불가, admin은 가능)
	@RequestMapping("mypage")
	public ModelAndView checkpage(String id,HttpSession session){
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		//admin인 경우 모든 주문내역 조회
		List<Sale> salelist = service.salelist(id);
		for(Sale sa : salelist) {
			List<SaleItem> saleitemlist = service.saleItemList(sa.getSaleid());
			for(SaleItem si : saleitemlist) {
				Item item = service.getItem(si.getItemid());
				si.setItem(item);
			}
			try {//User객체 등록
				User saleuser = service.getUser(sa.getUserid());
				sa.setUser(saleuser);
			}catch(LoginException e) {}
			sa.setItemList(saleitemlist);
		}
		try {
			String key = CipherUtil.makehash(id).substring(0,16);
			String email = CipherUtil.decrypt(user.getEmail(),key); //이메일복호화
			user.setEmail(email);
		}catch(Exception e) {
			e.printStackTrace();
		}
		mav.addObject("salelist",salelist);
		mav.addObject("user",user);
		return mav;
	}
	
	//delete.shop과 update.shop에서만 사용할 수 있게 수정 : * 는 상관없이 모두 사용
	@GetMapping(value= {"update","delete"}) //회원정보수정화면,탈퇴확인화면
	public ModelAndView checkview(String id,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		try {
			String key = CipherUtil.makehash(id).substring(0,16);
			String email = CipherUtil.decrypt(user.getEmail(),key); //이메일복호화
			user.setEmail(email);
		}catch(Exception e) {
			e.printStackTrace();
		}
		mav.addObject("user", user);
		return mav;
	}
	
	//1. 비밀번호 : 해쉬화 db에 저장
	//2. email : id의 해쉬값에서 키결정.
	//			  암호화 
	@PostMapping("update")//회원정보 수정 눌렀을시
	public ModelAndView checkupdate(@Valid User user, BindingResult bresult,HttpSession session){
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			return mav;
		}
		//비밀번호 검증 : 입력된 비밀번호 , db에 등록된 비밀번호 비교
		// 일치:update
		// 불일치:메세지를 유효성출력으로 화면에 출력
		User loginUser = (User)session.getAttribute("loginUser");//admin인지 확인하기 위해
		try{
			String pw = CipherUtil.makehash(user.getPassword());
			if(!pw.equals(loginUser.getPassword())){
				bresult.reject("error.login.password");
				return mav;
			}
			//useraccount 테이블에 내용 추가. login.jsp로이동
			//암호화 부분 추가
			String key = CipherUtil.makehash(user.getUserid()).substring(0,16);
			user.setEmail(CipherUtil.encrypt(user.getEmail(),key)); //이메일암호화
			service.userupdate(user);
			mav.setViewName("redirect:mypage.shop?id="+user.getUserid());
			if(!loginUser.getUserid().equals("admin")) {
				session.setAttribute("loginUser", user); //업데이트 했을때 login정보도 바꿔줌
			}
		}catch(Exception e) {
			e.printStackTrace();
			bresult.reject("error.user.update");
		}
		return mav;
	}
	
	//관리자도 강제탈퇴가능해야함, 유효성 검증 하지않았음
	@PostMapping("delete")//		 user를 받아와도 되고, String id, String password로 받아와도 됨
	public ModelAndView checkdelete(User user , HttpSession session) throws Exception{
		ModelAndView mav = new ModelAndView();
		User loginUser = (User)session.getAttribute("loginUser");
		
		String pw = CipherUtil.makehash(user.getPassword());
		if(!pw.equals(loginUser.getPassword())){
			throw new LoginException("비밀번호가 틀립니다.","delete.shop?id="+user.getUserid());
		}
		try {
			service.userDelete(user.getUserid());
			if(loginUser.getUserid().equals("admin")) {
				mav.setViewName("redirect:/admin/list.shop");
			}else {
				session.invalidate();
				mav.addObject("msg",user.getUserid()+"회원님. 탈퇴 되었습니다.");
				mav.addObject("url","login.shop");
				mav.setViewName("alert");
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new LoginException("회원 탈퇴시 오류가 발생했습니다. 전산부 연락 요망","delete.shop?id="+user.getUserid());
		}
		return mav;
	}
	
	@PostMapping(value="password",
				 produces="text/html;charset=UTF-8")//text/plain은 번역하지 않고 그대로 출력함
	@ResponseBody//해시맵객체로 만들어서 전달
	public String checkpassword(@RequestParam HashMap<String,String> param, HttpSession session) {
		User login = (User)session.getAttribute("loginUser");
		String hashpass = null;
		try {
			//param.get("pass") : request.getParameter("pass")
			hashpass = CipherUtil.makehash(param.get("pass"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(!login.getPassword().equals(hashpass)) {
			throw new LoginException("비밀번호 오류","password.shop");
		}
		String chgpass = null;
		try {
			chgpass = CipherUtil.makehash(param.get("chgpass"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		service.userPasswordUpdate(login.getUserid(),chgpass);
		login.setPassword(chgpass);
		//return 자체를 script로 전달 / view를 새로 만들지 않음
		return "<script>alert('비밀번호가 변경 되었습니다.')\n"
//				+ "opener.location.href='login.shop'\n"
				+ "self.close()\n"
				+ "</script>";
	}
}
