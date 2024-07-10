package clubbook.backend.bootstrap.data_dev;

import clubbook.backend.model.Role;
import clubbook.backend.model.RoleEnum;
import clubbook.backend.model.User;
import clubbook.backend.repository.RoleRepository;
import clubbook.backend.service.RoleService;
import clubbook.backend.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
public class DataGenerator {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostConstruct
    public void init() {

        RoleEnum[] rolesEnum = RoleEnum.values();
        Role role;
        for (RoleEnum roleEnumValue : rolesEnum) {
            role = new Role(roleEnumValue);
            this.roleService.save(role);
        }

        /*Role studentRole = roleService.findByName(RoleEnum.STUDENT);
        Role teacherRole = roleService.findByName(RoleEnum.TEACHER);

        User user = new User("Alessandro", "Nuzzi Herrero", "sandro.nuzzi.h@gmail.com", "abcd", "603636098", LocalDate.of(2001, 2, 16), teacherRole);
        User user1 = new User("Andrea", "Nuzzi Herrero", "andy.nuzzi@gmail.com", "abcd", "603636097", LocalDate.of(2000, 3, 8), studentRole);
        User user2 = new User("Wiktoria", "Rolewicz Kedzierska", "wikirole@gmail.com", "abcd", "634950950", LocalDate.of(2001, 7, 8), studentRole);
        User user3 = new User("Jose Luis", "Toledano", "jolutoher@gmail.com", "abcd", "611223122", LocalDate.of(2000, 9, 16), studentRole);
        User user4 = new User("Cristiano", "Ronaldo Dos Santos Aveiro", "cr7@gmail.com", "abcd", "714565714", LocalDate.of(1984, 2, 8), studentRole);
        userService.save(user);
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);*/
    }
}
