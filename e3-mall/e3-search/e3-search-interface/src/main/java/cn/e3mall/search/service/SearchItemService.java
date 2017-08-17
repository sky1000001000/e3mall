package cn.e3mall.search.service;

import cn.e3mall.common.utils.E3Result;

public interface SearchItemService {
 
	E3Result importAllItems();
	
	E3Result addDocument(long itemId) throws Exception;
}
