package com.qywenji.image.commons.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.google.common.collect.Lists;
import com.qywenji.image.commons.Constants;
import com.qywenji.image.commons.utils.ChangeValueType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Controller
@Scope("prototype")
public class BaseController
{
	private String commitToken;

	public void write(Object object,ServletResponse response)
	{
		try
		{
			String json =
			    JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
			response.setContentType("text/json;charset=UTF8");
			response.getWriter().write(json);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	public Map<String,Object> success(final Object data){
		return new HashMap<String,Object>(){
			private static final long serialVersionUID = 8794654518385734933L;
			{
				put(Constants.RESPONSE_CODE,Constants.RESPONSE_STATUS_SUCCESS );
				put(Constants.RESPONSE_DATA, data);
			}
		};
	}
	public Map<String,Object> success(){
		return success(null);
	}
	public Map<String,Object> error(final Object data){
		return new HashMap<String,Object>(){
			private static final long serialVersionUID = 8794654518385734933L;
			{
				put(Constants.RESPONSE_CODE, Constants.RESPONSE_STATUS_FAIL);
				put(Constants.RESPONSE_DATA, data);
			}
		};
	}	

	private Map<String, Map<String, Object>> makePageSearchCondition(Map<String, Object> dataMap)
	{
		if (dataMap == null || dataMap.isEmpty())
			return null;
		Set<Entry<String, Object>> entitySet = dataMap.entrySet();
		Map<String, Object> incondition = new HashMap<>();
		Map<String, Object> eqcondition = new HashMap<>();
		Map<String, Object> gtcondition = new HashMap<>();
		Map<String, Object> ltcondition = new HashMap<>();
		Map<String, Object> notcondition = new HashMap<>();
		Map<String, Object> likecondition = new HashMap<>();
		Map<String, Object> betweencondition = new HashMap<>();
		for (Entry<String, Object> entity : entitySet)
		{
			if (entity.getKey().startsWith(Constants.SEARCHCONDITION_FLAG))
			{
				if (entity.getKey().startsWith(Constants.SEARCHCONDITION_EQ))
				{
					if (entity.getValue() == null || "".equals(entity.getValue()))
						continue;
					String value = null;
					if (entity.getValue() instanceof String[])
					{
						String[] foo = (String[]) entity.getValue();
						if (foo[0] == null || "".equals(foo[0]))
							continue;
						value = foo[0];
					}
					else
					{
						value = String.valueOf(entity.getValue()).trim();
					}
					String key = entity.getKey().replace(Constants.SEARCHCONDITION_EQ, "");
					eqcondition.put(key.substring(2), ChangeValueType.changeValueType(key, value));
					continue;
				}
				if (entity.getKey().startsWith(Constants.SEARCHCONDITION_GT))
				{
					if (entity.getValue() == null || "".equals(entity.getValue()))
						continue;
					String value = null;
					if (entity.getValue() instanceof String[])
					{
						String[] foo = (String[]) entity.getValue();
						if (foo[0] == null || "".equals(foo[0]))
							continue;
						value = foo[0];
					}
					else
					{
						value = String.valueOf(entity.getValue()).trim();
					}
					String key = entity.getKey().replace(Constants.SEARCHCONDITION_GT, "");
					gtcondition.put(key.substring(2), ChangeValueType.changeValueType(key, value));
					continue;
				}
				if (entity.getKey().startsWith(Constants.SEARCHCONDITION_IN))
				{
					if (entity.getValue() == null || "".equals(entity.getValue()))
						continue;
					String value = null;
					if (entity.getValue() instanceof String[])
					{
						String[] foo = (String[]) entity.getValue();
						if (foo[0] == null || "".equals(foo[0]))
							continue;
						value = foo[0];
					}
					else
					{
						value = String.valueOf(entity.getValue()).trim();
					}
					String[] initems = value.split(",");
					String key = entity.getKey().replace(Constants.SEARCHCONDITION_IN, "");
					incondition.put(key.substring(2),
							ChangeValueType.changeValueType(key, Lists.newArrayList(initems)));
					continue;
				}
				if (entity.getKey().startsWith(Constants.SEARCHCONDITION_LT))
				{
					if (entity.getValue() == null || "".equals(entity.getValue()))
						continue;
					String value = null;
					if (entity.getValue() instanceof String[])
					{
						String[] foo = (String[]) entity.getValue();
						if (foo[0] == null || "".equals(foo[0]))
							continue;
						value = foo[0];
					}
					else
					{
						value = String.valueOf(entity.getValue()).trim();
					}
					String key = entity.getKey().replace(Constants.SEARCHCONDITION_LT, "");
					ltcondition.put(key.substring(2), ChangeValueType.changeValueType(key, value));
					continue;
				}
				if (entity.getKey().startsWith(Constants.SEARCHCONDITION_NOT))
				{
					if (entity.getValue() == null || "".equals(entity.getValue()))
						continue;
					String value = null;
					if (entity.getValue() instanceof String[])
					{
						String[] foo = (String[]) entity.getValue();
						if (foo[0] == null || "".equals(foo[0]))
							continue;
						value = foo[0];
					}
					else
					{
						value = String.valueOf(entity.getValue()).trim();
					}
					String key = entity.getKey().replace(Constants.SEARCHCONDITION_NOT, "");
					notcondition.put(key.substring(2), ChangeValueType.changeValueType(key, value));
					continue;
				}
				if (entity.getKey().startsWith(Constants.SEARCHCONDITION_LIKE))
				{
					if (entity.getValue() == null || "".equals(entity.getValue()))
						continue;
					String value = null;
					if (entity.getValue() instanceof String[])
					{
						String[] foo = (String[]) entity.getValue();
						if (foo[0] == null || "".equals(foo[0]))
							continue;
						value = foo[0];
					}
					else
					{
						value = String.valueOf(entity.getValue()).trim();
					}
					String key = entity.getKey().replace(Constants.SEARCHCONDITION_LIKE, "");
					likecondition.put(key.substring(2), ChangeValueType.changeValueType(key, value));
					continue;
				}
				if (entity.getKey().startsWith(Constants.SEARCHCONDITION_BETWEEN))
				{
					if (entity.getValue() == null || "".equals(entity.getValue()))
						continue;
					String value = null;
					if (entity.getValue() instanceof String[])
					{
						String[] foo = (String[]) entity.getValue();
						if (foo[0] == null || "".equals(foo[0]))
							continue;
						value = foo[0];
					}
					else
					{
						value = String.valueOf(entity.getValue()).trim();
					}
					String[] initems = value.split(",");
					String key = entity.getKey().replace(Constants.SEARCHCONDITION_BETWEEN, "");
					betweencondition.put(key.substring(2),
							ChangeValueType.changeValueType(key, Lists.newArrayList(initems)));
					continue;
				}
			}
		}
		Map<String, Map<String, Object>> searchCondition = new HashMap<>();
		if(!eqcondition.isEmpty())
			searchCondition.put(Constants.SEARCHCONDITION_EQ, eqcondition);
		if(!gtcondition.isEmpty())
			searchCondition.put(Constants.SEARCHCONDITION_GT, gtcondition);
		if(!incondition.isEmpty())
			searchCondition.put(Constants.SEARCHCONDITION_IN, incondition);
		if(!ltcondition.isEmpty())
			searchCondition.put(Constants.SEARCHCONDITION_LT, ltcondition);
		if(!notcondition.isEmpty())
			searchCondition.put(Constants.SEARCHCONDITION_NOT, notcondition);
		if(!likecondition.isEmpty())
			searchCondition.put(Constants.SEARCHCONDITION_LIKE, likecondition);
		if(!betweencondition.isEmpty())
			searchCondition.put(Constants.SEARCHCONDITION_BETWEEN, betweencondition);
		return searchCondition;
	}

	public Map<String, Map<String, ?>> createListAllProperties(ServletRequest request)
	{
		return createListAllProperties( this.makePageSearchCondition(createSearchDataMapFromRequest(request)),this.createOrderMapFromRequest(request));
	}
	

	public Map<String, Map<String, ?>> createListAllProperties(Map<String, ?> search,Map<String, ?> order)
	{
		Map<String, Map<String, ?>> result = new HashMap<>();
		result.put(Constants.SEARCHCONDITION, search);
		result.put(Constants.ORDER_CONDITION, order);
		return result;
	}
	
	private Map<String ,List<String>> createOrderMapFromRequest(ServletRequest request){
		Map<String, List<String>> data = new HashMap<>();
		String asc = request.getAttribute(Constants.ORDER_BY_ASC)+","+request.getParameter(Constants.ORDER_BY_ASC);
		String desc = request.getAttribute(Constants.ORDER_BY_DESC)+","+request.getParameter(Constants.ORDER_BY_DESC);
		List<String> ascPro = null;
		List<String> descPro = null;
		if(asc != null && !asc.isEmpty()){
			String[] foo = asc.split(",");
			ascPro = new ArrayList<>(foo.length);
			for(String f : foo){
				if(f == null || "null".equals(f))
					continue;
				ascPro.add(f);
			}
		}
		if(desc != null && !desc.isEmpty()){
			String[] foo = desc.split(",");
			descPro = new ArrayList<>(foo.length);
			for(String f : foo){
				if(f == null || "null".equals(f))
					continue;
				descPro.add(f);
			}
		}
		data.put(Constants.ORDER_ASC, ascPro);
		data.put(Constants.ORDER_DESC, descPro);
		return data;
	}
	
	public Map<String, Object> createSearchDataMapFromRequest(ServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		Enumeration<String> attrs = request.getAttributeNames();
		Enumeration<String> params= request.getParameterNames();
		while(attrs.hasMoreElements()){
			String name = attrs.nextElement();
			if(name.startsWith(Constants.SEARCHCONDITION_FLAG)){
				data.put(name, request.getAttribute(name));
			}
		}
		while(params.hasMoreElements()){
			String name = params.nextElement();
			if(name.startsWith(Constants.SEARCHCONDITION_FLAG)){
				data.put(name, request.getParameter(name));
			}
		}
		return data;
	}
	
	public String getCommitToken()
	{
		return commitToken;
	}

	public void setCommitToken(String commitToken)
	{
		this.commitToken = commitToken;
	}
	
}
