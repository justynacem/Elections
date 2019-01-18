package com.app.service;

import com.app.dto.MyModelMapper;
import com.app.dto.VoterDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Constituency;
import com.app.model.Token;
import com.app.model.Voter;
import com.app.repository.ConstituencyRepository;
import com.app.repository.TokenRepository;
import com.app.repository.VoterRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class VoterService {
    private VoterRepository voterRepository;
    private ConstituencyRepository constituencyRepository;
    private TokenRepository tokenRepository;
    private MyModelMapper modelMapper;

    public VoterService(VoterRepository voterRepository, ConstituencyRepository constituencyRepository, TokenRepository tokenRepository, MyModelMapper modelMapper) {
        this.voterRepository = voterRepository;
        this.constituencyRepository = constituencyRepository;
        this.tokenRepository = tokenRepository;
        this.modelMapper = modelMapper;
    }

    public VoterDto addVoter(VoterDto voterDto) {
        try {

            if (voterDto == null) {
                throw new NullPointerException("VOTER IS NULL");
            }
            if (voterDto.getAge() < 18) {
                throw new IllegalArgumentException("VOTER'S AGE < 18");
            }
            if (voterDto.getEducation() == null) {
                throw new NullPointerException("NULL VOTER'S EDUCATION");
            }
            if (voterDto.getGender() == null) {
                throw new NullPointerException("NULL VOTER'S GENDER");
            }
            if (voterDto.getConstituencyDto() == null) {
                throw new NullPointerException("NULL VOTER'S CONSTITUENCY");
            }

            Constituency constituency = constituencyRepository.findById(
                    voterDto.getConstituencyDto().getId())
                    .orElseThrow(NullPointerException::new);

            TokenService tokenService = new TokenService(tokenRepository, modelMapper);
            Token token = Token.builder()
                    .tokenValue(tokenService.newToken())
                    .expirationDate(LocalDateTime.now().plusHours(3))
                    .build();

            Voter voter = modelMapper.fromVoterDtoToVoter(voterDto);
            voter.setConstituency(constituency);

            token.setVoter(voter);
            voterRepository.save(voter);
            tokenRepository.save(token);
            return modelMapper.fromVoterToVoterDto(voter);

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, "ADD VOTER: " + e);
        }
    }
}
