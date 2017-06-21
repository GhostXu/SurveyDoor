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
	 * ����md5����
	 * @throws Exception 
	 */
	public static String md5(String src){
		try{
			StringBuffer sb = new StringBuffer();
			char[] chars = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
			//���һ�������㷨
			MessageDigest md = MessageDigest.getInstance("MD5");
			//ʹ�����ϼ����㷨��src���м��ܴ���
			byte[] data = md.digest(src.getBytes());
			for(byte b : data){
				//����λ������4λ����0000����
				sb.append(chars[(b >> 4) & 0x0F]);
				//����λ
				sb.append(chars[b & 0x0F]);
			}
			return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ��ȸ���,���Ƶ���������ͼ��ʹ�����л�����ʵ����ȸ��ƣ�.
	 * java-->byte[]:���л����̣� byte[]-->java�������л�����
	 * ע���������л��Ĺ�����������������
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
	 * ��String[]ת����String,","���
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
	 * ��ȡʵ�� id
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
