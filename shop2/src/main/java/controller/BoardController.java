package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import exception.LoginException;
import logic.Board;
import logic.Item;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public ModelAndView write() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Board());
		return mav;
	}
	
	
	/*
	 * 1. 유효성 검증
	 * 2. db의 board 테이블에 데이터 등록, 파일 업로드
	 * 3. 등록 성공 : list
	 * 4. 등록 실패 : write
	 * 
	 */
	@PostMapping("write")
	public ModelAndView writePost(@Valid Board board, BindingResult bresult,
			   HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		String boardid = (String)request.getSession().getAttribute("boardid");
		if(boardid == null) boardid="1";
		request.getSession().setAttribute("boardid", boardid);
		board.setBoardid(boardid);
		service.boardWrite(board,request);
		mav.setViewName("redirect:list?boardid="+boardid);
		return mav;
	}
	/*
	 * @RequestParam : 파라미터값을 객체에 매핑하여 저장하는 어노테이션
	 */
	@RequestMapping("list")
	public ModelAndView list(@RequestParam Map<String,String> param, HttpSession session) {
//	public ModelAndView list(@RequestParam("page") Integer pageNum,String boardid, HttpSession session) {
//		System.out.println(param);
		Integer pageNum = null;
		if (param.get("pageNum") != null)
		   pageNum = Integer.parseInt(param.get("pageNum"));
		String boardid = param.get("boardid");
		String searchtype = param.get("searchtype");
		String searchcontent = param.get("searchcontent");
		ModelAndView mav = new ModelAndView();
		if(pageNum == null || pageNum.toString().equals("")) {
			   pageNum = 1; 
		}
		if(boardid == null || boardid.equals("")) {
			boardid = "1"; 
		}
		session.setAttribute("boardid", boardid);
		if(searchtype == null ||  searchcontent == null || 
		   searchtype.trim().equals("") ||  searchcontent.trim().equals("")) {
			searchtype = null;
			searchcontent = null;
		}		
		String boardName=null;
		switch(boardid) {
			case "1" : boardName = "공지사항"; break;
			case "2" : boardName = "자유게시판"; break;
			case "3" : boardName = "QNA"; break;
		}
		int limit = 10;  //한페이지에 보여줄 게시물 건수
		int listcount = service.boardcount(boardid,searchtype,searchcontent); //등록 게시물 건수
		//boardlist : 현재 페이지에 보여줄 게시물 목록
		List<Board> boardlist = service.boardlist
				          (pageNum,limit,boardid,searchtype,searchcontent);
		//페이징 처리를 위한 값
		int maxpage = (int)((double)listcount/limit + 0.95); //등록 건수에 따른 최대 페이지
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1; // 페이지의 시작 번호
		int endpage = startpage + 9;                              // 페이지의 끝 번호
		if(endpage > maxpage) endpage = maxpage;         // 페이지의 끝 번호는 최대 페이지보다 클수 없다
		int boardno = listcount - (pageNum - 1) * limit; // 화면에 보여지는 게시물 번호
		//20230530
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());//오늘 날짜를 문자열로 저장
		mav.addObject("boardid",boardid);  
		mav.addObject("boardName", boardName); 
		mav.addObject("pageNum", pageNum); 
		mav.addObject("maxpage", maxpage); 
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage); 
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);
		mav.addObject("today", today); 
		return mav;		
	}
	
	@GetMapping("detail")
	public ModelAndView detail(Integer num) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num);
		service.addReadcnt(num);
		mav.addObject("board", board);
		
		if(board.getBoardid() == null || board.getBoardid().equals("1")) {
			mav.addObject("boardName", "공지사항"); 
		} else if (board.getBoardid().equals("2")) {
			mav.addObject("boardName", "자유게시판");
		} else if (board.getBoardid().equals("3")) {
			mav.addObject("boardName", "QnA");
		}
		return mav;
	}
	
	@GetMapping({"reply","update","delete"})
	public ModelAndView getBoard(Integer num, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String boardid = (String)session.getAttribute("boardid");
		Board board = service.getBoard(num);
		mav.addObject("board",board);
		if(boardid == null || boardid.equals("1"))
			mav.addObject("boardName","공지사항");
		else if (boardid.equals("2"))
			mav.addObject("boardName","자유게시판");
		else if (boardid.equals("3"))
			mav.addObject("boardName","QnA");
		return mav;
	}
	
	@PostMapping("reply")
	public ModelAndView replyPost(@Valid Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		
		if(bresult.hasErrors()) {
			Board dbboard = service.getBoard(board.getNum());
			Map<String,Object> map = bresult.getModel();
			Board b = (Board)map.get("board");
			b.setTitle(dbboard.getTitle());
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		
		try {
			service.boardReply(board);
			mav.setViewName("redirect:list?boardid" + board.getBoardid());
		} catch (Exception e){
			e.printStackTrace();
			throw new LoginException("답변 등록 실패.", "reply?num="+board.getNum());
		}
		
		return mav;
	}
	
	
	/*
	 * 1. 유효성 검증
	 * 2. 비밀번호 검증 => 오류 : '비밀번호가 틀립니다.', update로 이동
	 * 3. 비밀번호 검증 => 일치 : db내용 수정, 업로드된 파일이 있는 경우 파일 업로드도 진행
	 * 		=> 완료 : detail 페이지 이동
	 * 		=> 실패 : '게시글 수정 실패', update로 이동
	 * 
	 * 
	 */
	@PostMapping("update")
	public ModelAndView updatePost(@Valid Board board, BindingResult bresult, HttpServletRequest request) throws BoardException {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		
		Board dbboard = service.getBoard(board.getNum());
		
		if (!board.getPass().equals(dbboard.getPass())) {
			throw new BoardException("비밀번호가 다릅니다.", "update?num="+board.getNum());
		} 
		
		try {
			service.boardUpdate(board,request);
			mav.setViewName("redirect:detail?num=" + board.getNum());
		} catch (Exception e){
			e.printStackTrace();
			throw new BoardException("게시글 수정 실패.", "update?num="+board.getNum());
		}
		return mav;
	}
	
	@PostMapping("delete")
	public ModelAndView delete(Board board, BindingResult bresult) {
		// BindingResult는 객체 필요
		ModelAndView mav = new ModelAndView();
		if(board.getPass() == null || board.getPass().trim().equals("")) {
			bresult.reject("error.required.password");
			return mav;
		}
		
		Board dbboard = service.getBoard(board.getNum());
		
		if(!board.getPass().equals(dbboard.getPass())) {
			bresult.reject("error.board.password");
			return mav;
		}
		System.out.println(board.getBoardid());
		try {
			service.boardDelete(board.getNum());
			mav.setViewName("redirect:list?boardid="+dbboard.getBoardid());
		} catch (Exception e) {
			e.printStackTrace();
			bresult.reject("error.board.fail"); 
		}
		return mav;
	}
	
	@RequestMapping("imgupload")
	public String imgupload(MultipartFile upload, String CKEditorFuncNum,
			HttpServletRequest request, Model model) {
		
		String path = request.getServletContext().getRealPath("/")+"board/imgfile/";
		service.uploadFileCreate(upload, path);
		String fileName = request.getContextPath()+"/board/imgfile/"+upload.getOriginalFilename();
		model.addAttribute("fileName",fileName);
		
		return "ckedit"; // view이름, /WEB-INF/view/ckedit.jsp
		
		/*
		 * upload : CKEditor 모듈에서 업로르되는 이미지의 이름
		 * 			업로드되는 이미지 파일의 내용. 이미지값
		 * CKEditorFuncNum : CKEditor 모듈에서 파라미터로 전달되는 값, 리턴해야되는 값
		 * model : ModelAndView 중 Model에 해당하는 객체
		 * 		   View에 전달할 데이터정보가 저장되는 객체.
		 * return 타입(String) : 뷰의 이름
		 * 
		 */
		
	}
	


} // class