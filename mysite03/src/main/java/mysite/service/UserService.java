package mysite.service;

import org.springframework.stereotype.Service;

import mysite.repository.UserRepository;
import mysite.vo.UserVo;

@Service
public class UserService {
	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void join(UserVo userVo) {
		userRepository.insert(userVo);
	}

	public UserVo getUser(Long id) {
		return userRepository.findById(id);
	}
	
	public UserVo getUser(String email, String gpassword) {
		return userRepository.findByEmailAndPassword(email, gpassword);
	}

	public void update(UserVo userVo) {
		userRepository.update(userVo);
	}
}
