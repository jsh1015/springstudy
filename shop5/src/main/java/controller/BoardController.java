package controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import logic.Board;
import logic.ShopService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list")
	public ModelAndView list(Integer pageNum, HttpServletRequest request) {
		//String searchtype,String searchcontent
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		int limit = 10;
		
		String type = request.getParameter("searchtype");
		String content = request.getParameter("searchcontent");
		
		if(type==null||content==null) {
			type = null;
			content = null;
		}
		if(type!=null && type.equals("")) {
			type = null;
		}
		if(content!=null && content.equals("")) {
			content = null;
		}
		int listcount = service.boardcount(type,content); //전체 등록된 게시물 건수
		List<Board> boardlist = service.boardlist(pageNum,limit,type,content);
		// 최대페이지
		int maxpage = (int)((double)listcount/limit + 0.95);
		// 보여지는 첫번째 페이지
		int startpage = (int)((pageNum/10.0 + 0.9)-1) * 10 + 1;
		// 보여지는 마지막 페이지
		int endpage = startpage + 9;
		if(endpage > maxpage) endpage = maxpage;
		// 화면에 출력되는 게시물 번호. 순서 db와 상관없음
		int boardno = listcount - (pageNum -1) * limit;
		mav.addObject("pageNum",pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardlist",boardlist);
		mav.addObject("boardno",boardno);
		return mav;
	}
	
	
	@PostMapping("write")
	public ModelAndView write(@Valid Board board, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.boardWrite(board,request);
			mav.setViewName("redirect:list.shop");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시물 등록에 실패하였습니다.","write.shop");
		}
		return mav;
	}

	/*
	@GetMapping("*")
	public ModelAndView detail(int num) {
		//db에서 num에 해당하는 게시물을 읽어서 Board 객체에 저장
	}
	*/
	
//	@GetMapping("write")
	@GetMapping("*")
	public ModelAndView getBoard(Integer num, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		Board board= null;
		if(num == null) {
			board = new Board();
		}else {
			board = service.getBoard(num, request);
		}
		mav.addObject("board",board);
		return mav;
	}
	
	//CKEDITOR로 이미지 업로드
	@RequestMapping("imgupload")
	public String impupload(MultipartFile upload,String CKEditorFuncNum, 
			HttpServletRequest request, Model model) {
		//upload : 업로드된 이미지 정보 저장. 이미지 파일.
		String path = request.getServletContext().getRealPath("/")+"board/imgfile/";
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		if(!upload.isEmpty()) {
			//업로드될 파일을 저장할 File 객체 
			File file = new File(path, upload.getOriginalFilename());
			try {
				//업로드 파일 생성
				upload.transferTo(file);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		//절대경로 fullpath
		String fileName = "/shop4/board/imgfile/" + upload.getOriginalFilename();
		model.addAttribute("fileName",fileName);
		model.addAttribute("CKEditorFuncNum",CKEditorFuncNum);
		return "ckedit";
	}
	
	/*
		1. 파라미터 값 Board 객체 저장. 유효성 검증.
		2. 답변글 데이터 추가
			service.
			- grpstep 값보다 큰 grpstep을 +1 수정. (같은 grp인경우)
			- maxnum + 1 값으로 num값을 저장
			- grplevel + 1 값으로 grplevel 값을 저장
			- grpstep + 1 값으로 grpstep 값을 저장
			- 파라미터값으로 board 테이블에 insert 하기
		3. 추가 후 list.shop으로 페이지 요청
	 */
	@PostMapping("reply")
	public ModelAndView reply(@Valid Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			Board dbBoard = service.getBoard(board.getNum());
			Map<String, Object> map = bresult.getModel();
			Board b = (Board)map.get("board");
			b.setSubject(dbBoard.getSubject());
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			service.boardreply(board);
			mav.setViewName("redirect:list.shop");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("답글 등록에 실패하였습니다.","reply.shop");
		}
		return mav;
	}
	
	/*
		1. 파라미터 값 Board 객체 저장. 유효성 검증
		2. 입력된 비밀번호와 db의 비밀번호를 비교 비밀번호가 맞는 경우 3.
			틀린경우 '비밀번호가 틀립니다.', update.shop Get방식으로 호출
		3. 수정정보를 db에 변경
			- 첨부파일 변경 : 첨부파일 업로드, fileurl 정보 수정
		4. list.shop 페이지 요청
	 */
	@PostMapping("update")
	public ModelAndView update(@Valid Board board, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		Board b = service.getBoard(board.getNum()); //db
		if(!b.getPass().equals(board.getPass())) {
			throw new BoardException("비밀번호가 틀립니다.","update.shop?num="+board.getNum());
		}
		try {
			service.boardUpdate(board,request);
			mav.setViewName("redirect:list.shop");
		}catch(Exception e) {
			e.printStackTrace();
			throw new BoardException("게시물 수정에 실패하였습니다.","list.shop");
		}
		return mav;
	}
	
	/*
		1. num,pass 파라미터 저장
		2. db의 비밀번호와 입력된 비밀번호가 틀리면 error.login.password 코드입력.
		3. db에서 해당 게시물 삭제
			삭제실패:게시글 삭제 실패. delete.shop 페이지로 이동
			삭제성공:list.shop 페이지로 이동
	 */
	@PostMapping("delete")
	public ModelAndView delete(Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		Board b = service.getBoard(board.getNum());
		if(!b.getPass().equals(board.getPass())) {
			bresult.reject("error.login.password");
			return mav;
		}
		try {
			service.boardDelete(board.getNum());
			mav.setViewName("redirect:list.shop");
		}catch(Exception e) {
			throw new BoardException("게시글 삭제 실패.","delete.shop?num="+board.getNum());
		}
		return mav;
	}
}

