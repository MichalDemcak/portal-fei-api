package portal.fei.api.controllers.isp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import portal.fei.api.domain.Referent;
import portal.fei.api.repositories.interfaces.isp.ReferentRepository;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/api/referents")
@CrossOrigin("http://localhost:4200")
public class ReferentController {

    @Autowired
    private ReferentRepository repository;

    @GetMapping("")
    public ResponseEntity<List<Referent>> findAll(){
        return new ResponseEntity<>(this.repository.findAll(), HttpStatus.OK);
    }
}
