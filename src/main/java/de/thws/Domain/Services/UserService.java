package de.thws.Domain.Services;

import de.thws.Domain.DomainModels.User;
import de.thws.Ports.output.UserRepository;

import java.util.Optional;

public class UserService {
    private UserRepository userRepo;

    public UserService(UserRepository userRepo){
        this.userRepo= userRepo;
    }

    //Registration of the user UseCase1
    public User register (String username, String email, String password){
        if (userRepo.findByUsername(username).isPresent() || userRepo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = new User(username, email, password);
        userRepo.save(user);
        return user;
    }
    public User updateUser(Long userId, String username, String email, String password) {
        Optional<User> userOpt = userRepo.findById(userId); //looking for a user by ID
        if (userRepo.findById(userId).isEmpty()) { //if there's no such an ID throw an exception
            throw new IllegalArgumentException("User not found");
        }
            User user = userOpt.get(); //getting a user out of Optional
            //updating the user's data
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(password);
            userRepo.save(user);  //save the updated user in the variable user repository
            return user;
        }

        public void deleteUser(Long userId){
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()){
            throw new IllegalArgumentException("user not found"); //or code 404 lol
        }
        userRepo.deleteById(userId);
    }
}
