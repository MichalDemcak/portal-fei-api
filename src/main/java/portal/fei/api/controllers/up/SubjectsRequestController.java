package portal.fei.api.controllers.up;


import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import portal.fei.api.domain.up.SubjectsRequest;
import portal.fei.api.repositories.interfaces.up.SubjectsRequestRepository;

import java.util.List;

@Controller
@RequestMapping("/api/subjectsRequest")
@CrossOrigin("http://localhost:4200")
public class SubjectsRequestController {

    @Autowired
    private SubjectsRequestRepository repository;

    @PostMapping("")
    public ResponseEntity<Integer> create(@RequestBody SubjectsRequest subjectsRequest) {
        @Nullable
        SubjectsRequest stored = this.repository.save(subjectsRequest);

        return new ResponseEntity<Integer>(stored.getId(), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<SubjectsRequest>> all(){
        return new ResponseEntity<List<SubjectsRequest>>(this.repository.findAll(), HttpStatus.OK);
    }
}
