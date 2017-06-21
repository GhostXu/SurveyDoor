package cn.web;

import java.lang.reflect.ParameterizedType;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>,Preparable{
	
	private static final long serialVersionUID = -1314560516485837572L;
	
	public T model;
	
	@SuppressWarnings("unchecked")
	public BaseAction(){
		try{
			ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
			Class<T> classz = (Class<T>) type.getActualTypeArguments()[0];
			model = classz.newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public T getModel() {
		return model;
	}

	public void prepare() throws Exception {
	}
}
