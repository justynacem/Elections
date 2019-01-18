package com.app.service;

import com.app.dto.MyModelMapper;
import com.app.dto.PoliticalPartyDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Candidate;
import com.app.model.PoliticalParty;
import com.app.repository.CandidateRepository;
import com.app.repository.PoliticalPartyRepository;
import com.app.repository.VoteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PoliticalPartyService {
    private PoliticalPartyRepository politicalPartyRepository;
    private CandidateRepository candidateRepository;
    private VoteRepository voteRepository;
    private MyModelMapper modelMapper;

    public PoliticalPartyService(PoliticalPartyRepository politicalPartyRepository, CandidateRepository candidateRepository, VoteRepository voteRepository, MyModelMapper modelMapper) {
        this.politicalPartyRepository = politicalPartyRepository;
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
    }

    public void addPoliticalParty(PoliticalPartyDto politicalPartyDto) {
        try {
            if (politicalPartyDto == null) {
                throw new NullPointerException("NULL POLITICAL PARTY");
            }
            PoliticalParty politicalParty = modelMapper.fromPoliticalPartyDtoToPoliticalParty(politicalPartyDto);
            politicalPartyRepository.save(politicalParty);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD POLITICAL PARTY" + e);
        }
    }

    public List<PoliticalPartyDto> getAllPoliticalParties() {
        try {
            return politicalPartyRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromPoliticalPartyToPoliticalPartyDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET ALL POLITICAL PARTIES: " + e);
        }
    }

    public void deletePoliticalParty(Long politicalPartyId) {
        try {
            PoliticalParty politicalParty = politicalPartyRepository.findById(politicalPartyId)
                    .orElseThrow(NullPointerException::new);
            politicalPartyRepository.delete(politicalParty);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "DELETE POLITICAL PARTY: " + e);
        }
    }
}
