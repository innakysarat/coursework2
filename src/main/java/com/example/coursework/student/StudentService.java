package com.example.coursework.student;

import com.example.coursework.bucket.BucketName;
import com.example.coursework.filestore.FileStore;
import com.example.coursework.internships.Internship;
import com.example.coursework.internships.InternshipRepository;
import com.example.coursework.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class StudentService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InternshipRepository internshipRepository;
    private final FileStore fileStore;

    @Autowired
    public StudentService(UserRepository userRepository, PasswordEncoder passwordEncoder, InternshipRepository internshipRepository, FileStore fileStore) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.internshipRepository = internshipRepository;
        this.fileStore = fileStore;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new IllegalStateException("User doesn't exist");
        }
    }

    public void addUser(User user) {
        String role;
        if (user.getUsername().endsWith("admin")) {
            role = "ADMIN";
        } else if (user.getUsername().endsWith("leader")) {
            role = "LEADER";
        } else {
            role = "STUDENT";
        }
        if (role.equals("ADMIN")) {
            user.setRole("ADMIN");
            user.setGrantedAuthorities(UserRole.ADMIN.getGrantedAuthorities());
        } else if (role.equals("LEADER")) {
            user.setRole("LEADER");
            user.setGrantedAuthorities(UserRole.LEADER.getGrantedAuthorities());
        } else {
            user.setRole("STUDENT");
            user.setGrantedAuthorities(UserRole.STUDENT.getGrantedAuthorities());
        }
        userRepository.save(user);
    }

    public void deleteStudent(Integer studentId) {
        boolean exists = userRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " doesn't exist");
        }
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " doesn't exist"));
        if (user.getRole().equals("ADMIN")) {
            throw new IllegalStateException("Can't delete admin");
        }
        userRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudentByAdmin(Integer studentId, String name, String surname, String patronymic,
                                     String email, String phone, String username, String password,
                                     LocalDate dayOfBirth) {
        Optional<User> userOptional = userRepository.findById(studentId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            updateStudent(user, name, surname, patronymic, email, phone, username, password, dayOfBirth);
        }
    }

    @Transactional
    public void updateStudentByHimself(Integer user_id, String username, String name, String surname, String patronymic,
                                       String email, String phone, String username_update, String password,
                                       LocalDate dayOfBirth) {
        User user = userRepository.findByUsername(username);
        User user_byId = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalStateException("Student with id " + user_id + " doesn't exist"));
        if (!user.equals(user_byId)) {
            throw new IllegalStateException("User can not update someone else's information");
        } else {
            updateStudent(user, name, surname, patronymic, email, phone, username_update, password, dayOfBirth);
        }
    }

    @Transactional
    public void updateStudent(User user, String name, String surname, String patronymic,
                              String email, String phone, String username, String password,
                              LocalDate dayOfBirth) {

        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }
        if (surname != null &&
                surname.length() > 0 &&
                !Objects.equals(user.getSurname(), surname)) {
            user.setSurname(surname);
        }
        if (patronymic != null &&
                patronymic.length() > 0 &&
                !Objects.equals(user.getPatronymic(), patronymic)) {
            user.setPatronymic(patronymic);
        }

        if (dayOfBirth != null &&
                !Objects.equals(user.getDayOfBirth(), dayOfBirth)) {
            user.setDayOfBirth(dayOfBirth);
        }
        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(user.getEmail(), email)) {
            User student
                    = userRepository.findByEmail(email);
            if (student != null) {
                throw new IllegalStateException("Email is taken");
            }
            user.setEmail(email);
        }
        if (phone != null &&
                phone.length() > 0 &&
                !Objects.equals(user.getPhone(), phone)) {
            User student
                    = userRepository.findByPhone(phone);
            if (student != null) {
                throw new IllegalStateException("Phone is taken");
            }
            user.setPhone(phone);
        }
        if (username != null &&
                username.length() > 0 &&
                !Objects.equals(user.getUsername(), username)) {
            User anotherUser
                    = userRepository.findByUsername(username);
            if (anotherUser != null) {
                throw new IllegalStateException("Username is taken");
            }
            user.setUsername(username);
        }
        if (password != null &&
                password.length() > 0 &&
                !Objects.equals(user.getPassword(), password)) {
            user.setPassword(password);
        }
        userRepository.save(user);
    }

    public void addFavourites(String username, String internship_name) {
        User user = userRepository.findByUsername(username);
        Internship internship = internshipRepository.findByName(internship_name);
        if (user != null && internship != null) {
            // user.addFavourites(internship);
            internship.addUsers(user);
            // userRepository.save(user);
            // internshipRepository.save(internship);
        } else {
            throw new IllegalStateException("User/internship not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        String role = user.getRole();
       /* if (username.endsWith("admin")) {
            role = "ADMIN";
        } else if (username.endsWith("leader")) {
            role = "LEADER";
        } else {
            role = "STUDENT";
        }*/
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (role.equals("ADMIN")) {
            for (SimpleGrantedAuthority s : UserRole.ADMIN.getGrantedAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(s.getAuthority()));
            }
            user.setGrantedAuthorities(UserRole.ADMIN.getGrantedAuthorities());
        } else if (role.equals("LEADER")) {
            for (SimpleGrantedAuthority s : UserRole.LEADER.getGrantedAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(s.getAuthority()));
            }
            user.setGrantedAuthorities(UserRole.LEADER.getGrantedAuthorities());
        } else {
            for (SimpleGrantedAuthority s : UserRole.STUDENT.getGrantedAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(s.getAuthority()));
            }
            user.setGrantedAuthorities(UserRole.STUDENT.getGrantedAuthorities());
        }
        String password = passwordEncoder.encode(user.getPassword());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), password, grantedAuthorities);
    }

    public Set<Internship> getFavourites(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getInternships();
        } else {
            throw new IllegalStateException("User doesn't exist");
        }
    }

    public void uploadUserImage(Integer user_id, MultipartFile file) {
        // 1. Check if image is not empty
        isFileEmpty(file);
        // 2. If file is an image
        isImage(file);

        // 3. The user exists in our database
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 4. Grab some metadata from file if any
            Map<String, String> metadata = extractMetadata(file);

            // 5. Store the image in s3 and update database (userImageLink) with s3 image link
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUser_id());
            String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

            try {
                fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
                user.setUserImageLink(filename);
                userRepository.save(user);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    byte[] downloadUserImage(Integer user_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String path = String.format("%s/%s",
                    BucketName.PROFILE_IMAGE.getBucketName(),
                    user.getUser_id());
            return user.getUserImageLink()
                    .map(key -> fileStore.download(path, key))
                    .orElse(new byte[0]);
        } else {
            throw new IllegalStateException("Failed to download image");
        }

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }
}