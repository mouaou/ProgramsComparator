/*package programs.comparator.com.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import programs.comparator.com.dao.ProgramRepository;
import programs.comparator.com.entities.Program;

import java.util.List;

@Service
@Transactional

public class ProgramService {

@Autowired
private ProgramRepository programRepo;

    public List<Program> listAll() {
        return programRepo.findAll();
    }

    public void save(Program product) {
        programRepo.save(product);
    }

    public Program get(Long id) {
        return programRepo.findById(id).get();
    }

    public void delete(Long id) {
        programRepo.deleteById(id);
    }
}
*/