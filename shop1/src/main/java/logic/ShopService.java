package logic;
// shop1/src/main/java/logic/ShopService.java

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import dao.UserDao;
import dao.boardDao;
import dao.saleDao;
import dao.saleItemDao;

@Service // @Component + Service(Controller 기능과 dao 기능의 중간역할) 기능을 함.
public class ShopService {
	@Autowired // 객체 주입
	private  ItemDao itemDao; // ItemDao 객체를 받은 ShopService 객체를 주입
	
	@Autowired // 객체 주입
	private  UserDao userDao;
	
	@Autowired // 객체 주입
	private  saleDao saleDao;
	
	@Autowired // 객체 주입
	private  saleItemDao saleItemDao;
	
	@Autowired // 객체 주입
	private  boardDao boardDao;
	
	public List<Item> itemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.getItem(id);
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			String path = request.getServletContext().getRealPath("/")+"img/";
			uploadFileCreate(item.getPicture(),path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
			// 업로드된 파일 이름
		}
		int maxid = itemDao.maxId(); // item 테이블에 저장된 최대 id 값
		item.setId(maxid+1); // 최대 id 값 +1
		itemDao.insert(item);
	}

	public void uploadFileCreate(MultipartFile file, String path) {
		// file : 파일의 내용
		// path : 업로드 하려는 폴더
		String orgFile = file.getOriginalFilename();
		File f = new File(path);
		if(f.exists()) f.mkdirs();
		try {
			file.transferTo(new File(path+orgFile));
			// transferTo : file에 저장된 내용을 파일로 저장
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			String path = request.getServletContext().getRealPath("/")+"img/";
			uploadFileCreate(item.getPicture(),path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}

	public void itemDelete(Integer id) {
		itemDao.delete(id);
	}

	public void userinsert(User user) {
		userDao.insert(user);
	}

	public User selectUserOne(String userid) {
		return userDao.selectOne(userid);
	}

	/*
	 * 1. 로그인 정보, 장바구니 정보 => sale, saleitem 테이블에 데이터 저장
	 * 2. 결과는 Sale 객체에 저장
	 * 		- sale 테이블 저장 : saleid값 구하기. 최대값+1
	 * 
	 * 
	 * 
	 */
	public Sale checkend(User loginUser, Cart cart) {
		int maxsaleid = saleDao.getMaxSaleId();
		Sale sale = new Sale();
		sale.setSaleid(maxsaleid+1);
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());
		saleDao.insert(sale);
		
		int seq = 0;
		for(ItemSet is : cart.getItemSetList()) {
			SaleItem saleItem = new SaleItem(sale.getSaleid(),++seq,is);
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem);
		}
		return sale; // 주문 정보, 주문상품 정보, 상품 정보, 사용자 정보
	}

	public List<Sale> salelist(String userid) {
		List<Sale> list = saleDao.list(userid);//id 사용자가 주문 정복목록 
		for(Sale sa : list) {
			//saleitemlist : 한개의 주문에 해당하는 주문상품 목록
			List<SaleItem> saleitemlist =saleItemDao.list(sa.getSaleid());
			for(SaleItem si : saleitemlist) {
				Item item = itemDao.getItem(si.getItemid()); //상품정보
				si.setItem(item);
			}
			sa.setItemList(saleitemlist);
		}
		return list;
	}

	public void userUpdate(@Valid User user) {
		userDao.update(user);
	}

	public void userDelete(String userid) {
		userDao.delete(userid);
		
	}

	public void userChgpass(String userid,String chgpass) {
		userDao.chgpass(userid,chgpass);
		
	}
	
	public List<User> userList() {
		// TODO Auto-generated method stub
		return userDao.userList();
	}

	public List<User> getUserList(String[] idchks) {
		return userDao.list(idchks);
	}

	public String getSearch(User user) {
		return userDao.search(user);
	}

	public void boardWrite(@Valid Board board, HttpServletRequest request) {
		int maxnum = boardDao.maxNum(); //등록된 게시물의 최대 num값 리턴
		board.setNum(++maxnum);
		board.setGrp(maxnum);
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = request.getServletContext().getRealPath("/")+"board/file/";
			this.uploadFileCreate(board.getFile1(),path);
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.insert(board);
	}

	public int boardcount(String boardid, String searchtype,String searchcontent) {
		return boardDao.count(boardid,searchtype,searchcontent);
	}

	public List<Board> boardlist(Integer pageNum, int limit, String boardid, String searchtype,String searchcontent) {
		return boardDao.list(pageNum,limit,boardid,searchtype,searchcontent);
	}

	public Board getBoard(Integer num) {
		return boardDao.selectOne(num);
	}
	
	public void addreadcnt(Integer num) {
		boardDao.addReadcnt(num);
	}
	
	@Transactional //트랜잭션 처리함. 업무를 원자화(all or nothing) 
	public void boardReply(Board board) {
	      boardDao.updateGrpStep(board);
	      int max = boardDao.maxNum();
	      board.setNum(++max);
	      board.setGrplevel(board.getGrplevel() + 1);
	      board.setGrpstep(board.getGrpstep() + 1);
	      boardDao.insert(board);
	   }

	public void boardUpdate(Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = request.getServletContext().getRealPath("/")+"board/file/";
			this.uploadFileCreate(board.getFile1(),path);
			board.setFileurl((board.getFile1().getOriginalFilename()));
			
		}
		
		boardDao.boardUpdate(board,request);
		
	}

	public void boardDelete(int num) {
		boardDao.boardDelete(num);
		
	}





}
