package mk.ukim.finki.usersmanagement.domain.repositories;

import mk.ukim.finki.usersmanagement.domain.dtos.UserDTO;
import mk.ukim.finki.usersmanagement.domain.dtos.UserFilter;
import mk.ukim.finki.usersmanagement.domain.models.Privilege;
import mk.ukim.finki.usersmanagement.domain.models.User;
import mk.ukim.finki.usersmanagement.domain.models.ids.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {

    @Query(
            value = "select distinct u from User u " +
                    "left join Person p on u.person.id =  p.id " +
                    "left join Role r on r.id = u.role.id " +
                    "where " +
                    "(:#{#userFilter.getEmail().isEmpty()} = true or lower(u.email) like %:#{#userFilter.getEmail()}%) " +
                    "and " +
                    "(:#{#enabledCheck == -1} = true or  (:#{#enabledCheck == 0} = true and u.enabled = false) or (:#{#enabledCheck == 1} = true and u.enabled = true)) " +
                    "and " +
                    "(:#{#userFilter.getFirstName().isEmpty()} = true or lower(p.firstName) like %:#{#userFilter.getFirstName()}%) " +
                    "and " +
                    "(:#{#userFilter.getLastName().isEmpty()} = true or lower(p.lastName) like %:#{#userFilter.getLastName()}%) " +
                    "and " +
                    "(:#{#userFilter.getPhoneNumber().isEmpty()} = true or lower(p.phoneNumber) like %:#{#userFilter.getPhoneNumber()}%) " +
                    "and " +
                    "(:#{#userFilter.getRole().isEmpty()} = true or lower(r.label) like %:#{#userFilter.getRole()}%)"
    )
    Page<User> findAllPaged(UserFilter userFilter, int enabledCheck, Pageable pageable);

    @Query(
            value = "select distinct u from User u " +
                    "left join Person p on u.person.id =  p.id " +
                    "left join Role r on r.id = u.role.id " +
                    "where " +
                    "(:#{#userFilter.getEmail().isEmpty()} = true or lower(u.email) like %:#{#userFilter.getEmail()}%) " +
                    "and " +
                    "(:#{#enabledCheck == -1} = true or  (:#{#enabledCheck == 0} = true and u.enabled = false) or (:#{#enabledCheck == 1} = true and u.enabled = true)) " +
                    "and " +
                    "(:#{#userFilter.getFirstName().isEmpty()} = true or lower(p.firstName) like %:#{#userFilter.getFirstName()}%) " +
                    "and " +
                    "(:#{#userFilter.getLastName().isEmpty()} = true or lower(p.lastName) like %:#{#userFilter.getLastName()}%) " +
                    "and " +
                    "(:#{#userFilter.getPhoneNumber().isEmpty()} = true or lower(p.phoneNumber) like %:#{#userFilter.getPhoneNumber()}%) " +
                    "and " +
                    "(:#{#userFilter.getRole().isEmpty()} = true or lower(r.label) like %:#{#userFilter.getRole()}%)"
    )
    List<User> findAllFiltered(UserFilter userFilter, int enabledCheck);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    @Query("select distinct rp.privilege from User u " +
            "join Role r on r = u.role " +
            "join RolePrivilege rp on rp.role = r " +
            "where u = :user")
    List<Privilege> getUserPrivileges(User user);
}
