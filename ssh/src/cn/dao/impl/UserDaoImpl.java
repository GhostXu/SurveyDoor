package cn.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.entity.User;

@Repository(value="userDao")
public class UserDaoImpl extends BaseDaoImpl<User>{

	@Resource
	private SessionFactory sessionFactory;
	
	public boolean regEmail(String email) {
		Query query = sessionFactory.getCurrentSession().createQuery(" from User where email=?" + email);
		int result = query.executeUpdate();
		if(result == 0){
			return true;
		}else{
			return false;
		}
	}
}
