package cn.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

public abstract class BaseEntity implements Serializable{

	private static final long serialVersionUID = 736512887759653113L;
	
	public abstract Integer getId() ;

	public abstract void setId(Integer id) ;
	
	public String toString(){
		//user{id:*,uname:***,....}
		StringBuffer sb = new StringBuffer();
		String className = this.getClass().getSimpleName();
		sb.append(className + "{");
		
		try {
			Field[] fields = this.getClass().getDeclaredFields() ;
			Class ftype = null ;
			Object fvalue = null ;
			for(Field f : fields){
				ftype = f.getType();
				//ֻ��ȡʵ�����е� �����͡��������ͺͷǾ�̬ ������
				if((ftype.isPrimitive() || ftype == String.class 
						|| ftype == Integer.class || ftype == Long.class 
						|| ftype == Date.class) && !Modifier.isStatic(f.getModifiers())){
					f.setAccessible(true);
					fvalue = f.get(this);
					sb.append(f.getName() +":"+fvalue + ",");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append("}");
		return sb.toString().substring(0, sb.length()-1);
	}
}