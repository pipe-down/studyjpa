package downstagram.downstagram.service;

import downstagram.downstagram.domain.TableStatus;
import downstagram.downstagram.domain.User;
import downstagram.downstagram.model.UserDto;
import downstagram.downstagram.model.UserRegistrationModel;
import downstagram.downstagram.repository.UserRepository;
import downstagram.downstagram.utils.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public List<User> list() {
        return userRepository.findAll();
    }

    public User login(String userId, String password) {
        User user = userRepository.findUserByUserId(userId);

        if (user == null) return null;

        String pw = EncryptionUtils.encryptMD5(password);

        if (!user.getPassword().equals(pw)) return null;

        return user;
    }

    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    public User findByUserId(String userId) {
        User user = userRepository.findUserByUserId(userId);
        return user;
    }

    public List<UserDto> findByUserIdContainsOrNameContains(String word1, String word2) {
        List<User> users = userRepository.findByUserIdContainsOrNameContains(word1, word2);
        return users.stream().map(UserDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void save(UserRegistrationModel userModel) {
        validateDuplicateMember(userModel);
        User user = User.createUser(userModel.getUserid(), userModel.getPasswd1(), userModel.getName(), null, userModel.getPhone(), null);

        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void profile_update(String userId, String name, String website, String introduce) {
        User user = findByUserId(userId);
        user.updateUser(name, website, introduce);
        userRepository.save(user);
    }

    @Transactional
    public void profile_image(String userId, String profileImg) {
        User user = findByUserId(userId);
        user.setProfileImg(profileImg);
        userRepository.save(user);
    }

    public boolean enableCheck(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user.getEnable() == TableStatus.Y) {
            return true;
        }
        return false;
    }

    @Transactional
    public void enable_user(Long id, int enableValue) {
        User user = findById(id);
        user.enableUser(enableValue);
        userRepository.save(user);
    }

    private void validateDuplicateMember(UserRegistrationModel userModel) {
        List<User> user = userRepository.findByUserId(userModel.getUserid());
        if (user.size()>0) { // 유저 중복 체크
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
