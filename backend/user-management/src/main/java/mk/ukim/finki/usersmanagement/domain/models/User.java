package mk.ukim.finki.usersmanagement.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import mk.ukim.finki.emt.productscatalog.domain.models.RatingsAndReviews;
import mk.ukim.finki.dailycheckinsmanagement.domain.models.UserDailyCheckIn;
import mk.ukim.finki.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.usersmanagement.domain.models.ids.UserId;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "ur_users", schema = "userroles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity<UserId> {

    private String email;
    private String password;
    private OffsetDateTime dateCreated;
    private OffsetDateTime dateModified;
    private Boolean enabled;
    private Double creditBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ur_person_id")
    private Person person;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Token> tokens;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserDailyCheckIn> userDailyCheckIns;
}
