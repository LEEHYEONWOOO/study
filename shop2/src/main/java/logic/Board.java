package logic;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;
/*
 * lombok : setter, getter, toString, 생성자등을 자동 생성해주는 유틸리티
 * 
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Board {
	private int num;
	private String boardid;
	
	@NotEmpty(message="글쓴이를 입력하세요.")
	private String writer;
	
	@NotEmpty(message="비밀번호를 입력하세요.")
	private String pass;
	
	@NotEmpty(message="제목을 입력하세요.")
	private String title;
	
	@NotEmpty(message="내용을 입력하세요.")
	private String content;
	private MultipartFile file1;
	private String fileurl;
	private Date regdate;
	private int readcnt;
	private int grp;
	private int grplevel;
	private int grpstep;
	private int commcnt;
	
	
	

}
