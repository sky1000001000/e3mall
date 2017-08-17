package cn.e3mall.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;


import cn.e3mall.search.service.impl.SearchItemServiceImpl;

public class ItemAddMessageListener implements MessageListener {
    @Autowired
    private SearchItemServiceImpl searchItemServiceImpl;
	@Override
	public void onMessage(Message message) {
		 
		try {
			TextMessage textMessage = null;
			Long itemId = null;
			if(message instanceof TextMessage){
				textMessage = (TextMessage)message;
				itemId = Long.parseLong(textMessage.getText());
			}
			searchItemServiceImpl.addDocument(itemId);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}

}
