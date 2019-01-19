package com.hit.dm;

import java.io.Serializable;

import com.hit.dm.DataModel;

@SuppressWarnings("serial")
public class DataModel<T> extends Object implements Serializable {

	private Long dataModelId;
	private T content;

	public DataModel(java.lang.Long dataModelId, T content) {
		this.dataModelId = dataModelId;
		this.content = content;
	}

	public T getContent() {
		return content;
	}

	public Long getDataModelId() {
		return dataModelId;
	}

	@Override
	public int hashCode() {
		int code = content != null ? content.hashCode() : 0;
		code += 31 * dataModelId;
		return code;
	}

	@Override
	public String toString() {
		return "dataModelId:" + dataModelId + "content" + content.toString();

	}

	public void setDataModelId(Long dataModelId) {
		this.dataModelId = dataModelId;
	}

	public void setContent(T content) {
		this.content = content;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof DataModel) {
			DataModel other = (DataModel) obj;
			return other.getContent().equals(this.getContent()) && other.getDataModelId().equals(this.getDataModelId());
		}
		return false;
	}
}
