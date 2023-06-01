package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.BoardException;
import exception.LoginException;
import logic.Board;
import logic.ShopService;

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
	 * 2. db의 board 테이블에 내용 저장, 파일 업로드
	 * 3. 등록 성공 : list
	 * 	  등록 실패 : write
	 */
	@PostMapping("write")
	public ModelAndView writePost(@Valid Board board, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
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
	 * @RequestParam : 파라미터값을 저장하는 Map 객체
	 */
	@RequestMapping("list")
	public ModelAndView list(@RequestParam Map<String,String> param, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Integer pageNum = null;
		String searchtype = param.get("searchtype");
		String searchcontent = param.get("searchcontent");
		if(param.get("pageNum")!=null){pageNum = Integer.parseInt(param.get("pageNum"));}
		String boardid = param.get("boardid");
		if(pageNum == null || pageNum.toString().equals("")) {
			pageNum = 1;
		}
		if(boardid == null || boardid.equals("")) {
			boardid = "1";
		}
		session.setAttribute("boardid", boardid);
		if(searchtype ==null || searchcontent==null || searchtype.trim().equals("") || searchcontent.trim().equals("")) {
			searchtype = null;
			searchcontent = null;
		}
		String boardName=null;
		switch(boardid) {
		case "1" : boardName = "공지사항"; break;
		case "2" : boardName = "자유게시판"; break;
		case "3" : boardName = "QNA"; break;
		}
		int limit = 10;
		int listcount = service.boardcount(boardid,searchtype,searchcontent);
		List<Board> boardlist = service.boardlist(pageNum,limit,boardid,searchtype,searchcontent);
		int maxpage = (int)((double)listcount/limit+0.95);
		int startpage = (int)((pageNum/10.0 + 0.9)-1)*10+1;
		int endpage = startpage+9;
		if(endpage>maxpage) endpage = maxpage;
		int boardno = listcount - (pageNum-1)*limit;
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		mav.addObject("boardid",boardid);
		mav.addObject("boardName",boardName);
		mav.addObject("pageNum",pageNum);
		mav.addObject("maxpage",maxpage);
		mav.addObject("startpage",startpage);
		mav.addObject("endpage",endpage);
		mav.addObject("listcount",listcount);
		mav.addObject("boardlist",boardlist);
		mav.addObject("boardno",boardno);
		mav.addObject("today",today);
		mav.addObject("searchtype",searchtype);
		mav.addObject("searchcontent",searchcontent);
		return mav;
	}
	@GetMapping("detail")
	public ModelAndView detail(Integer num) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num); //num 게시판 내용 조회 
		service.addreadcnt(num);   //조회수 1 증가
		mav.addObject("board",board);
		if(board.getBoardid() == null || board.getBoardid().equals("1"))
			mav.addObject("boardName","공지사항");
		else if(board.getBoardid().equals("2"))
			mav.addObject("boardName","자유게시판");
		else if(board.getBoardid().equals("3"))
			mav.addObject("boardName","QNA");
		return mav;
	}
	
	@GetMapping({"reply","update","delete"})
	   public ModelAndView replyPost(Integer num, HttpSession session) {
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
	    	  b.setTitle(dbboard.getTitle()); //원글의 제목으로 변경
	    	  
	         mav.getModel().putAll(bresult.getModel() );
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
	    *	1.유효성 검증
	    *	2.비밀번호 검증 => 검증오류 : 비밀번호가 틀립니다. 메세지출력. update페이지 이동
	    *	3.db의 내용 수정. 업로드된 파일이 있는 경우는 파일업로드
	    *	4.수정완료 : detail 페이지 이동
	    *	  숮어실패 : 수정 실패 메세지 출력 후 update페이지 이동 
	    */
	   @PostMapping("update")
	   public ModelAndView update(@Valid Board board, BindingResult bresult,HttpServletRequest request) {
	      ModelAndView mav = new ModelAndView();
	      if(bresult.hasErrors()) { 
				mav.getModel().putAll(bresult.getModel());
				return mav; 
	      }
	      Board dbboard = service.getBoard(board.getNum());
	      if(!board.getPass().equals(dbboard.getPass())) {
	    	  throw new BoardException("비밀번호가 달라요","update?num="+board.getNum());
	      }
	      try {
	    	  service.boardUpdate(board,request);
	    	  mav.setViewName("redirect:detail?num="+board.getNum());
		  } catch (Exception e){
			  e.printStackTrace();
			  throw new BoardException("게시물 수정 실패.", "update?num="+board.getNum());
		  }
	      return mav;
	   }
	   /*
	    * 1. num,pass 파라미터 저장
	    * 2. 비밀번호 검증 : db에서 num에 해당하는 게시글 조회,db에 등록된 비밀번호와 입력된 비밀번호 비교
	    * 		비밀번호 오류 : error.board.password 코드값 설정 => delete.jsp로 전달
	    * 3. 비밀번호 일치 : db에서 num 게시글 삭제
	    * 		삭제성공 : list 페이지이동
	    * 		삭제실패 : delete 페이지이동
	    */
	   @PostMapping("delete")
	   public ModelAndView delete(Board board, BindingResult bresult) {
		   ModelAndView mav = new ModelAndView();
		   Board dbboard = service.getBoard(board.getNum());
		   
		   //if(board.getBoardid().equals("1") && ) {		   }
		   if(board.getPass()==null || board.getPass().trim().equals("")) { //비밀번호 입력 검증
			   bresult.reject("error.required.password");
			   return mav;
		   }
		   if(!board.getPass().equals(dbboard.getPass())) { //비밀번호 일치 검증
			   bresult.reject("error.board.password");
			   return mav;
		   }
		   try {
		    	  service.boardDelete(board.getNum());
		    	  mav.setViewName("redirect:list?num="+dbboard.getBoardid());
			  } catch (Exception e){
				  e.printStackTrace();
				  bresult.reject("error.board.fail");
			  }
		   return mav;
	   }
	   
	   @RequestMapping("imgupload")
	   public String imgupload(MultipartFile upload,String CKEditorFuncNum, HttpServletRequest request, Model model) {
		   /*
		    * upload : CKEditor 모듈에서 업로드되는 이미지의 이름
		    * 			업로드되는 이미지파일의 내용. 이미지값
		    * CKEditorFuncNum : CKEditor 모듈에서 파라미터로 전달되는 값. 리턴해야되는 값
		    * model : ModelAndView 중 Model에 해당하는 객체
		    * 			뷰에 전달할 데이터 정보 저장할 객체
		    */
		   String path = request.getServletContext().getRealPath("/")+"board/imgfile/";
		   service.uploadFileCreate(upload, path);
		   String fileName = request.getContextPath()+"/board/imgfile/"+upload.getOriginalFilename();
		   model.addAttribute("fileName",fileName);
		   return "ckedit"; //view 이름
	   }
	   
	   
	   
}
