package cn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Set;

import cn.entity.BaseEntity;
import cn.entity.security.Right;

public class DataUtils {
	/**
	 * 采用md5加密
	 * @throws Exception 
	 */
	public static String md5(String src){
		try{
			StringBuffer sb = new StringBuffer();
			char[] chars = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			//获得一个加密算法
			MessageDigest md = MessageDigest.getInstance("MD5");
			//使用以上加密算法对src进行加密处理
			byte[] data = md.digest(src.getBytes());
			for(byte b : data){
				//高四位向右移4位，与0000相与
				sb.append(chars[(b >> 4) & 0x0F]);
				//低四位
				sb.append(chars[b & 0x0F]);
			}
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 深度复制,复制的整个对象图（使用序列化技术实现深度复制）.
	 * java-->byte[]:序列化过程； byte[]-->java：反序列化过程
	 * 注：对象序列化的过程是由虚拟机来完成
	 */
	public static Serializable deeplyCopy(Serializable src){
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(src);
			oos.close();
			bos.close();
			byte[] data = bos.toByteArray();
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bis);
			Serializable copy = null;
			copy = (Serializable) ois.readObject();
			ois.close();
			bis.close();
			return copy;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 把String[]转换成String,","间隔
	 * @param value
	 * @return
	 */
	public static String strList2str(Object[] value) {
		StringBuffer sb = new StringBuffer();
		if(ValidateUtil.isValid(value)){
			for(Object s:value){
				sb.append(s);
				sb.append(",");
			}
			return sb.substring(0, sb.length()-1);
		}
		return null;
	}
	
	/**
	 * 抽取实体 id
	 * @param baseEntity
	 * @return
	 */
	public static String extractEntityId(Collection<? extends BaseEntity> entities) {
		if(!ValidateUtil.regList(entities)){
			return null;
		}else{
			StringBuffer sb = new StringBuffer();
			for(BaseEntity r : entities){
				sb.append(r.getId()).append(",");
			}
			return sb.toString().substring(0,sb.toString().length() - 1);
		}
	}
}
