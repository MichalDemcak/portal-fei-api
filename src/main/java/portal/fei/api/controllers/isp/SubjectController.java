package portal.fei.api.controllers.isp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import portal.fei.api.domain.isp.Subject;
import portal.fei.api.repositories.interfaces.isp.SubjectRepository;

import java.util.List;

@Controller
@RequestMapping("/api/subjects")
@CrossOrigin("http://localhost:4200")
public class SubjectController {

    @Autowired
    private SubjectRepository repository;

    @GetMapping("")
    public ResponseEntity<List<Subject>> getAll(){
        return new ResponseEntity<>(this.repository.findAll(), HttpStatus.OK);
    }
}
