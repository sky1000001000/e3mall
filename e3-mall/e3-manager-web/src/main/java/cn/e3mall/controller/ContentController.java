package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;
    /**
     * 添加内容
     * @param content
     * @return
     */
    @RequestMapping(value="content/save",method=RequestMethod.POST)
    @ResponseBody
	public E3Result addContent(TbContent content){
		
		E3Result E3Result = contentService.addContent(content);
		
		return E3Result;
		
	}
}
