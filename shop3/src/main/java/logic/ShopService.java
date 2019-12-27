package logic;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;

@Service //@Component + service 기능 : controller와 db의 중간역할
public class ShopService {
	@Autowired //내컨테이너안의 객체중 자료형이 ItemDao인 것을 주입 ->@service
	private ItemDao itemDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SaleDao saleDao;
	@Autowired
	private SaleItemDao saleItemDao;

	public List<Item> getItemList() {
		return itemDao.list();
	}

	public void itemCreate(Item item, HttpServletRequest request) { //item의 내용을 등록
		//업로드된 이미지 파일이 존재할 때
		if(item.getPicture()!=null && !item.getPicture().isEmpty()) {
			//파일 업로드 : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(item.getPicture(),request,"item/img/");
							// ↑ picture을 파일화, 경로지정
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.insert(item);
	}
	//↑
	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		//picture : 업로드된 파일의 내용
		String orgFile = picture.getOriginalFilename();
		String uploadPath = request.getServletContext().getRealPath("/") + path; //파일을 만들어줌
		File fpath = new File(uploadPath);
		if(!fpath.exists()) fpath.mkdirs(); //해당 path가없으면 생성
		try {
			//picture에 있는 것 파일로 생성
			picture.transferTo(new File(uploadPath + orgFile));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public Item getItem(String string) {
		return itemDao.selectOne(string);
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		//파일업로드가 있으면 업로드, 없으면 db만 수정
		//수정된 이미지 존재
		if(item.getPicture()!=null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}

	public void itemDelete(int id) {
		itemDao.delete(id);
	}

	public void userInsert(User user) {
		userDao.insert(user);
	}

	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}

	public Sale checkend(User loginUser, Cart cart) {
		Sale sale = new Sale();
		sale.setSaleid(saleDao.getMaxSaleId()); //saleid값 + 1
		sale.setUser(loginUser); //구매자 정보(로그인정보)
		sale.setUserid(loginUser.getUserid()); //구매자 id값만
		sale.setUpdatetime(new Date());//주문시간
		saleDao.insert(sale);
		//주문상품정보를 cart에서 조회
		List<ItemSet> itemList = cart.getItemSetList();
		int i = 0;
		for(ItemSet is : itemList) {
			int saleItemId = ++i;//					1,			1,		ItemSet객체
			SaleItem saleItem = new SaleItem(sale.getSaleid(),saleItemId,is);
			sale.getItemList().add(saleItem);//첫번째 상품 추가
			saleItemDao.insert(saleItem);//db에 추가
		}
		return sale;
	}

	public List<Sale> salelist(String id) {
		return saleDao.list(id); //사용자 id
	}

	public List<SaleItem> saleItemList(int saleid) {
		return saleItemDao.list(saleid); //saleid : 주문번호
	}

	public void userupdate(User user) {
		userDao.update(user);
	}

	public void userDelete(String userid) {
		userDao.delete(userid);
	}
}
