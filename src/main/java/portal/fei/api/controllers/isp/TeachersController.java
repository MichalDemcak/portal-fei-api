package portal.fei.api.controllers.isp;

import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portal.fei.api.domain.isp.Teachers;
import portal.fei.api.repositories.interfaces.isp.TeachersRepository;

import java.util.List;

@Controller
@RequestMapping("/api/teachers")
@CrossOrigin("http://localhost:4200")
public class TeachersController {

    @Autowired
    private TeachersRepository repository;


    @PostMapping("")
    public ResponseEntity<String> create(@RequestBody Teachers teachers) {
        @Nullable
        Teachers stored = this.repository.save(teachers);

        return new ResponseEntity<String>(stored.getId(), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Teachers>> all(){
        return new ResponseEntity<List<Teachers>>(this.repository.findAll(), HttpStatus.OK);
    }
}
