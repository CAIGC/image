package com.qywenji.image.commons.bean;

import java.util.List;

public class DataTableReturnObject<T>
{
    private Long totalRecords;
    private List<T> data;

    public DataTableReturnObject()
    {

    }

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public DataTableReturnObject(Long totalRecords, List<T> data) {
		super();
		this.totalRecords = totalRecords;
		this.data = data;
	}


}
