package com.app.service;

import com.app.dto.ConstituencyDto;
import com.app.dto.MyModelMapper;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Constituency;
import com.app.repository.ConstituencyRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConstituencyService {
    private ConstituencyRepository constituencyRepository;
    private MyModelMapper modelMapper;

    public ConstituencyService(ConstituencyRepository constituencyRepository, MyModelMapper modelMapper) {
        this.constituencyRepository = constituencyRepository;
        this.modelMapper = modelMapper;
    }

    public void addConstituency(ConstituencyDto constituencyDto) {
        try {
            if (constituencyDto == null) {
                throw new NullPointerException("NULL CONSTITUENCY");
            }
            Constituency constituency = modelMapper.fromConstituencyDtoToConstituency(constituencyDto);
            constituencyRepository.save(constituency);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD CONSTITUENCY: " + e);
        }
    }

    public List<ConstituencyDto> getAllConstituencies() {
        try {
            return constituencyRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromConstituencyToConstituencyDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET ALL CONSTITUENCIES: " + e);
        }
    }

    public void deleteConstituency(Long constituencyId) {
        try {
            Constituency constituency = constituencyRepository.findById(constituencyId)
                    .orElseThrow(NullPointerException::new);
            constituencyRepository.delete(constituency);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE CONSTITUENCY: " + e);
        }
    }
}
