package com.ibeacon.model.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *<pre>
 * 作用:通用基础类，实现Serializable接口
 * 注意:需要数据库持久化的JavaBean必须继承该类
 * 其他:继承该类后主键Id自动生成，创建时间自动生成
 *</pre>
 *
 * @author  chenwentao
 * @version 1.0, 2016-4-12
 * @see
 * @since
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", updatable = false, nullable = false)
	protected Date createTime = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}