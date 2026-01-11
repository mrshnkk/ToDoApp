package de.thws.Adapters.persistence_out;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="UserId", nullable = false)
    private Long userId;

    @Column(name="Username", nullable = false)
    private String username;

    @Column(name="Email", nullable = false)
    private String email;

    @Column(name="PasswordHash", nullable = false)
    private String passwordHash;

    @Column(name= "CreatedAt", nullable = false)
    private LocalDateTime createdAt;

    public UserEntity(){}

    public UserEntity(String username, String email, String passwordHash, LocalDateTime createdAt){
        this.username=username;
        this.email= email;
        this.passwordHash= passwordHash;
        this.createdAt=createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    @Override
    public String toString(){
        return "User id: " + userId + "\nUsername: " + username+ "\nEmail: " + email+ "\nAccount is created at: "+  createdAt;
    }
}
