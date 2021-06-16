package app.model.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "bank_account")
public class BankAccount implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "number", unique = true)
    String number;

    @Column(name = "user_id")
    int userId;

    @Column(name = "is_blocked")
    boolean isBlocked = false;
}
