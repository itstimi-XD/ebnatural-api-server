package ebnatural.bizcurator.apiserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Getter
@NoArgsConstructor
public class CertificationNumber extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Length(max = 320, message = "이메일은 320자리를 넘을 수 없습니다.")
    String username;
    @NotBlank
    String certificationNumber;

    public CertificationNumber(String username, String certificationNumber) {
        this.username = username;
        this.certificationNumber = certificationNumber;
    }

    public static CertificationNumber of(String username, String certificationNumber, BCryptPasswordEncoder passwordEncoder){
        return new CertificationNumber(username, passwordEncoder.encode(certificationNumber));
    }

    public boolean isExpired(){
        return Duration.between(super.getCreatedAt(), LocalDateTime.now()).getSeconds() > 3600;
    }
}
