package portal.fei.api.controllers.isp;

import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portal.fei.api.domain.isp.StudentRequestStates;
import portal.fei.api.repositories.interfaces.isp.StudentRequestStatesRepository;

import java.util.List;

@Controller
@RequestMapping("/api/requestState")
@CrossOrigin("http://localhost:4200")
public class StudentRequestStatesController {

    @Autowired
    private StudentRequestStatesRepository repository;

    @PostMapping("")
    public ResponseEntity<Long> create(@RequestBody StudentRequestStates requestStates){
        @Nullable
        StudentRequestStates stored = this.repository.save(requestStates);

        return new ResponseEntity<Long>(stored.getId(), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<StudentRequestStates>> all(){
        return new ResponseEntity<List<StudentRequestStates>>(this.repository.findAll(), HttpStatus.OK);
    }
}
