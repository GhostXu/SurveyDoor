package cn.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import cn.entity.security.Right;
import cn.entity.security.Role;

public class User implements Serializable{
	private static final long serialVersionUID = -3049052872923303342L;
	private Integer uid;
	private String nickName;
	private String password;
	private String email;
	private Date reDate;
	
	private boolean superAdmin;
	
	private Set<Role> roles;
	
	//�û�Ȩ���ܺ�
	private long[] rightSum ;
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getReDate() {
		return reDate;
	}
	public void setReDate(Date reDate) {
		this.reDate = reDate;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public boolean isSuperAdmin() {
		return superAdmin;
	}
	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}
	
	public long[] getRightSum() {
		return rightSum;
	}
	public void setRightSum(long[] rightSum) {
		this.rightSum = rightSum;
	}
	/**
	 * �����û�����Ȩ�޵Ķ����ƺ�
	 * @return
	 */
	public void calculateRight(){
		int pos = 0 ;
		long code = 0 ;
		for(Role r : roles){
			//��������Ա��
			if("-1".equals(r.getRoleValue())){
				this.superAdmin = true ;
				roles = null ;//��������
				return ;
			}else{
				for(Right right : r.getRights()){
					pos = right.getRightPos();
					code = right.getRightCode();
					rightSum[pos] = rightSum[pos] | code;
				}
			}
		}
	}
	/**
	 * �ж��û��Ƿ���Ȩ�ޣ�true��ʾ�У�false��ʾû��
	 * @return
	 */
	public boolean getRight(Right r) {
		int pos = r.getRightPos();
		long code = r.getRightCode();
		long ret = (rightSum[pos] & code) ;
		return !(ret == 0) ;
	}
}
