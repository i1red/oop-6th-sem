package app.model.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "card")
public class Card implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "number", unique = true)
    String number;

    @Column(name = "balance")
    double balance;

    @Column(name = "account_id", nullable = false)
    int accountId;
}
