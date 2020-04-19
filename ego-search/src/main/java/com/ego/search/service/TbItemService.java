package com.ego.search.service;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

public interface TbItemService {
	/**
	 * 初始化solr
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	void init() throws SolrServerException, IOException;
	/**
	 * 分页查询
	 * @param query
	 * @param page
	 * @param rows
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	Map<String, Object> selByQuery(String query,int page,int rows) throws SolrServerException, IOException;
	/**
	 * 新增
	 * @param map
	 * @param desc
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	int add(Map<String, Object> map,String desc) throws SolrServerException, IOException;
}
