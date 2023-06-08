package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.Item;
import logic.SaleItem;

public interface SaleItemMapper {
	
	
	//String sql = "insert into saleitem (saleid, seq, itemid, quantity)"
	//	+ " values (:saleid, :seq, :itemid, :quantity)";
	@Insert("insert into saleitem (saleid,seq,itemid,quantity) "
			+ " values(#{saleid},#{seq},#{itemid},#{quantity})")
	void insert(SaleItem saleItem);
	
	
	
	//String sql = "select * from saleitem where saleid=:saleid";
	@Select("select * from saleitem where saleid=#{saleid}")
	List<SaleItem> list(int saleid);

	
	
	
	
}
