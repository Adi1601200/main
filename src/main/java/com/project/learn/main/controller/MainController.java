package com.project.learn.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nimbusds.jose.proc.SecurityContext;
import com.project.learn.main.dto.Example;
import com.project.learn.main.entity.Post;
import com.project.learn.main.entity.PostRequest;
import com.project.learn.main.entity.RoleType;
import com.project.learn.main.entity.UserEntity;
import com.project.learn.main.kakfa.KafkaProducer;
import com.project.learn.main.repository.PostRepo;
import com.project.learn.main.repository.UserEntityRepo;
import com.project.learn.main.service.CustomUserService;
import com.project.learn.main.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private JwtUtil jwtUtil;



    private KafkaProducer kafkaProducer;

    @Autowired
    PostRepo postRepo;

    @Autowired
    UserEntityRepo userEntityRepo;

    @Autowired
    public MainController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/token")
    public ResponseEntity<?> generateToken(@RequestParam String userName,@RequestParam String password){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));
        }catch (Exception e){

        }
        UserDetails userDetails = customUserService.loadUserByUsername(userName);
        String token = null;
        if(userDetails.getPassword().equals(password)){
             token = jwtUtil.generateToken(userDetails);
        }else{
            ResponseEntity.badRequest();
        }


        return ResponseEntity.ok(token);


    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestParam String userName,@RequestParam String password,@RequestParam String role){
        List<UserEntity> list = userEntityRepo.findByUserName(userName);
        if(!list.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        userEntity.setRoleType(RoleType.valueOf(role));
        userEntityRepo.save(userEntity);
        UserDetails userDetails = customUserService.loadUserByUsername(userName);
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }




    @PutMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String id) throws JsonProcessingException {
        Optional<Post> post = postRepo.findById(Long.valueOf(id));
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        if(post.isPresent()){
            String json = ow.writeValueAsString(post.get());
            kafkaProducer.sendMessage(json);
            return ResponseEntity.ok("Message sent");
        }
        return ResponseEntity.badRequest().body("Message not sent");
    }

    @GetMapping(value = "/hello")
    public String hel(){
        return "Hi";
        // alternatively can be used to save gmail details after redirected from google
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MENTEE"))) {
            Post post = new Post();
            post.setCreatedBy(auth.getPrincipal().getClass().getName());
            post.setLink(postRequest.getLink());
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            Post save = postRepo.save(post);
            return ResponseEntity.ok(save);
        }
        return ResponseEntity.badRequest().body("UnAccessible");
    }

    @PostMapping("/post/accept")
    public ResponseEntity<?> acceptPost(@RequestParam String id,@RequestParam String userName,@RequestHeader String token){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("MENTOR"))) {
            Optional<Post> post = postRepo.findById(Long.valueOf(id));

            UserDetails userDetails = customUserService.loadUserByUsername(userName);
            Boolean validated = jwtUtil.validateToken(token, userDetails);
            if(post.isPresent() && validated){
                Post post1 = post.get();
                post1.setAcceptedBy(userName);
                postRepo.save(post1);
                return ResponseEntity.ok("Saved Details");
            }
            return ResponseEntity.badRequest().body("Bad Request");
        }
        return ResponseEntity.badRequest().body("UnAccessible");
    }


}
