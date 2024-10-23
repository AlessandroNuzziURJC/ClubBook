package clubbook.backend.service;

import clubbook.backend.model.ClassGroup;
import clubbook.backend.model.Role;
import clubbook.backend.model.User;
import clubbook.backend.repository.ClassGroupRepository;
import clubbook.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassGroupRepository classGroupRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }

    public User findById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public Page<User> getStudentsPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAllStudentsByOrderByNameAsc(pageable);
    }

    private String modifySearch(String search) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < search.length(); i++) {
            char currentChar = search.charAt(i);
            if ((currentChar >= 'a' && currentChar <= 'z')|| (currentChar >= 'A' && currentChar <= 'Z')) {
                output.append(currentChar);
            } else {
                output.append('_');
            }
        }
        return '%' + output.toString() + '%';
    }

    public List<User> getStudentsListFilteredByName(String search) {
        String searchMod = modifySearch(search);
        return userRepository.findAllStudentsByOrderByNameAscWithSearch(searchMod);
    }

    public Page<User> getTeachersPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAllTeachersByOrderByNameAsc(pageable);
    }

    public List<User> getTeachersListFilteredByName(String search) {
        String searchMod = modifySearch(search);
        return userRepository.findAllTeachersByOrderByNameAscWithSearch(searchMod);
    }

    public List<User> getAllTeachers() {
        return userRepository.findAllTeachers();
    }

    public List<User> getAllStudentsWithoutClassGroup() {
        return userRepository.findAllStudentsWithoutClassGroup();
    }

    public List<User> addNewStudentsClassGroup() {
        return null;
    }

    public List<User> findUserBornBetween(LocalDate birthYearStart, LocalDate birthYearEnd, Role role) {
        return this.userRepository.findAllUsersBornBetweenWithRole(birthYearStart, birthYearEnd, role.getName().name());
    }

    public List<User> findAllAdministrators() {
        return this.userRepository.findAllAdministrators();
    }

    public List<User> findAllAdministratorsExceptId(int id) {
        return this.userRepository.findAllAdministratorsExceptId(id);
    }

    private User extractUser(Integer id) {
        User user = this.userRepository.findById(id).orElseThrow();
        List<ClassGroup> classGroups;
        classGroups = this.classGroupRepository.findByStudentId(id);
        if (!classGroups.isEmpty()) {
            return null;
        }
        classGroups = this.classGroupRepository.findByTeacherId(id);
        if (!classGroups.isEmpty()) {
            return null;
        }
        return user;
    }

    public Boolean changeStatusUser(Integer id) {
        User user = this.extractUser(id);
        if (user == null) {
            return false;
        }
        user.setAllowedAccess(false);
        this.userRepository.save(user);
        return true;
    }

    public Boolean deleteUser(Integer id) {
        User user = this.extractUser(id);
        if (user == null) {
            return false;
        }
        this.userRepository.delete(user);
        return true;
    }

    @Transactional
    public void removeUsers() {
        this.userRepository.deleteByAllowedAccessFalse();
    }
}
