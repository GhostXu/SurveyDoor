package cn.service;

import cn.entity.User;

public interface UserService extends BaseService<User>{

	public boolean regEmail(String email);

	public User getEntityByParams(String email, String password);

	public void updateAuthorize(User model, Integer[] ownRoleIds);

	public void clearAuthorize(Integer userId);

}
