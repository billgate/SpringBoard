package kojh.spring.board;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import kojh.db.beans.BoardBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BbsController
{
	@Autowired
	BoardService boardService;
			
	/*
	 Spring 3 ���ĺ��ʹ� Bean Validation�� ���� ǥ���� ������ JSR-303 Spec.�� �����ϰ� �ִ�. 
	 Validation�� �������� ���¿� ���α׷����� ���·� ������ �� ������ Hibernate Validator�� ���� 
	 JSR-303 Spec.�� ������ ����ü�� �����Ͽ� ó���ȴ�.
	 
	 ��, Hibernate �߰��ؼ� Validation ó��
	*/
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/show_write_form", method = RequestMethod.GET)
	public String show_write_form( Model model) 
	{
		logger.info("show_write_form called!!");
				
		// ��ü�� �����ؼ� ���� ���;� ��!!!		
		model.addAttribute("boardBeanObjToWrite", new BoardBean());
					
		return "writeBoard";		
	}
		
	//test---> OK!!
	/*
	@RequestMapping( value = "/DoWriteBoard" ,method = RequestMethod.POST)
	//public String PostWork( @ModelAttribute("boardBeanObjToWrite") BoardBean boardBeanObj, Model model) //OK!! 
	public String PostWork( BoardBean boardBeanObjToWrite, Model model) //OK Too!!
	{		
		logger.info("PostWork called!!");
		logger.info("memo=["+boardBeanObjToWrite.getMemo()+"]");
		return "PostWork";
	}
	*/
	
	
	@RequestMapping(value = "/DoWriteBoard", method = RequestMethod.POST)
	public String DoWriteBoard( BoardBean boardBeanObjToWrite, Model model) // �̰͵� �����ϰ� ��ó�� @ModelAttribute ����ص� ��
	{
		logger.info("DoWriteBoard called!!");
		logger.info("memo=["+boardBeanObjToWrite.getMemo()+"]");
						
		//����!!
		boardService.insertBoard(boardBeanObjToWrite);
				
		//Date date = new Date();
		//DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, new Locale("ko"));		
		//String formattedDate = dateFormat.format(date);		
		//model.addAttribute("serverTime", formattedDate );
		
		//����� ��ȸ�� ���� ��Ŵ.
		model.addAttribute("totalCnt", new Integer(boardService.getTotalCnt(null)) ); //Integer objects
		model.addAttribute("boardList", boardService.getList(1, null, 2) ); // 1 page ������ ���(2�� rows)
			
		return "home";		
	}
	
	//���� ��� ��ȸ
	@RequestMapping(value = "/viewWork", method = RequestMethod.GET)
	public String viewWork	( 
								@RequestParam("memo_id") String memo_id,
								@RequestParam("current_page") String current_page,
								 Model model
							) 
	{
		logger.info("viewWork called!!");
		logger.info("memo_id=["+ memo_id+"] current_page=["+current_page+"]");
			
		model.addAttribute("memo_id", memo_id ); 
		model.addAttribute("current_page", current_page ); 
		model.addAttribute("boardData", boardService.getSpecificRow(memo_id) ); 
		
			
		return "viewMemo";		
	}
	
	// Ư�� ���������� �۾��� ������� ���ð��, ���� ������ ��ȣ�� �����ؼ� �ش� ������ ���
	@RequestMapping(value = "/listSpecificPageWork", method = RequestMethod.GET)
	public String listSpecificPageWork	(								
								@RequestParam("current_page") String current_page,
								Model model
							) 
	{
		logger.info("listSpecificPageWork called!!");
		logger.info("current_page=["+current_page+"]");
				 
		model.addAttribute("totalCnt", new Integer(boardService.getTotalCnt(null)) );
		model.addAttribute("current_page", current_page ); 
		model.addAttribute("boardList", boardService.getList( Integer.parseInt(current_page), null, 2)); 
					
		return "listSpecificPage";		
	}
	
	// Ư�� ������ ������ ���� ���� ���
	@RequestMapping(value = "/listSpecificPageWork_to_update", method = RequestMethod.GET)
	public String listSpecificPageWork_to_update	(	
								@RequestParam("memo_id") String memo_id,
								@RequestParam("current_page") String current_page,
								Model model
							) 
	{
		logger.info("listSpecificPageWork_to_update called!!");
		logger.info("memo_id=["+ memo_id+"] current_page=["+current_page+"]");
			
		model.addAttribute("memo_id", memo_id ); 
		model.addAttribute("current_page", current_page ); 
		model.addAttribute("boardData", boardService.getSpecificRow(memo_id) ); 
		
			
		return "viewMemoForUpdate";		
	}
	
	//���� row ������Ʈ
	@RequestMapping(value = "/DoUpdateBoard", method = RequestMethod.POST)
	public String DoUpdateBoard( 
			                    BoardBean boardBeanObjToUpdate, 
			                    @RequestParam("memo_id") String memo_id,
			                    @RequestParam("current_page") String current_page,
								Model model) 
	{
		logger.info("DoUpdateBoard called!!");
		logger.info("listSpecificPageWork_to_update called!!");
		logger.info("memo_id=["+ memo_id+"] current_page=["+current_page+"]");
		logger.info("memo=["+boardBeanObjToUpdate.getMemo()+"]");						
		
		/*
		boardService.updateBoard(boardBeanObjToUpdate);				
		*/
		model.addAttribute("totalCnt", new Integer(boardService.getTotalCnt(null)) );
		model.addAttribute("current_page", current_page ); 
		model.addAttribute("boardList", boardService.getList( Integer.parseInt(current_page), null, 2));
			
		return "listSpecificPage";		
	}
			
}