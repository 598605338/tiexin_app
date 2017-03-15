package com.linjia.web.query;

import com.linjia.base.query.Query;

public class SearchQuery extends Query{

   
    private String keyword;
    private String mallCode;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMallCode() {
		return mallCode;
	}

	public void setMallCode(String mallCode) {
		this.mallCode = mallCode;
	}
	
	

    
}