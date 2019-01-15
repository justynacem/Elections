package com.app.service;

import com.app.dto.CandidateDto;
import com.app.dto.MyModelMapper;
import com.app.dto.VoteDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Candidate;
import com.app.model.Constituency;
import com.app.model.PoliticalParty;
import com.app.model.Vote;
import com.app.repository.CandidateRepository;
import com.app.repository.ConstituencyRepository;
import com.app.repository.PoliticalPartyRepository;
import com.app.repository.VoteRepository;
import com.app.utils.FileManager;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidateService {
    private CandidateRepository candidateRepository;
    private ConstituencyRepository constituencyRepository;
    private PoliticalPartyRepository politicalPartyRepository;
    private VoteRepository voteRepository;
    private FileManager fileManager;
    private MyModelMapper modelMapper;

    public CandidateService(CandidateRepository candidateRepository, ConstituencyRepository constituencyRepository, PoliticalPartyRepository politicalPartyRepository, VoteRepository voteRepository, FileManager fileManager, MyModelMapper modelMapper) {
        this.candidateRepository = candidateRepository;
        this.constituencyRepository = constituencyRepository;
        this.politicalPartyRepository = politicalPartyRepository;
        this.voteRepository = voteRepository;
        this.fileManager = fileManager;
        this.modelMapper = modelMapper;
    }

    public void addCandidate(CandidateDto candidateDto) {
        try {
            if (candidateDto == null) {
                throw new NullPointerException("CANDIDATE IS NULL");
            }
            if (candidateDto.getAge() < 18) {
                throw new IllegalArgumentException("CANDIDATE'S AGE < 18");
            }
            if (candidateDto.getConstituencyDto() == null) {
                throw new NullPointerException("NULL CANDIDATE'S CONSTITUENCY");
            }
            if (candidateDto.getName() == null) {
                throw new NullPointerException("NULL CANDIDATE'S NAME");
            }
            if (candidateDto.getSurname() == null) {
                throw new NullPointerException("NULL CANDIDATE'S SURNAME");
            }
            if (candidateDto.getPoliticalPartyDto() == null) {
                throw new NullPointerException("NULL CANDIDATE'S POLITICAL PARTY");
            }

            Candidate candidate = modelMapper.fromCandidateDtoToCandidate(candidateDto);
            Constituency constituency = constituencyRepository
                    .findById(candidateDto.getConstituencyDto().getId())
                    .orElseThrow(NullPointerException::new);
            PoliticalParty politicalParty = politicalPartyRepository
                    .findById(candidateDto.getPoliticalPartyDto().getId())
                    .orElseThrow(NullPointerException::new);

            String filename = fileManager.addFile(candidateDto.getFile());

            candidate.setPoliticalParty(politicalParty);
            candidate.setConstituency(constituency);
            candidate.setPhoto(filename);

            candidateRepository.save(candidate);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD CANDIDATE: " + e);
        }
    }

    public List<CandidateDto> getAllCandidates() {
        try {
            return candidateRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromCandidateToCandidateDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET ALL CANDIDATES: " + e);
        }
    }

    public Map<CandidateDto, VoteDto> getAllCandidatesByVotes() {
        try {
            Map<Candidate, Vote> map = voteRepository
                    .findAll()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> candidateRepository.findById(e.getCandidate().getId()).get(),
                            Function.identity(),
                            (v1, v2) -> v1,
                            () -> new HashMap<>()
                    ));

            return map
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(e -> e.getValue().getVotes(), Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
                            e -> modelMapper.fromCandidateToCandidateDto(e.getKey()),
                            e -> modelMapper.fromVoteToVoteDto(e.getValue()),
                            (v1, v2) -> v1,
                            () -> new LinkedHashMap<>()
                    ));

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "GET ALL CANDIDATES BY VOTES: " + e);
        }
    }
}
