package com.gogym.apiserver.service;

import com.gogym.apiserver.dto.user.UserSaveRequestDto;
import com.gogym.apiserver.entity.User;
import com.gogym.apiserver.error.common.ErrorCode;
import com.gogym.apiserver.error.exception.PhoneNumberDuplicateException;
import com.gogym.apiserver.repository.UserRepository;
import com.gogym.apiserver.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User signup(UserSaveRequestDto requestDto) {
        if (userRepository.findByUserPhone(requestDto.getPhone()).orElse(null) != null) {
            throw new PhoneNumberDuplicateException("email duplicated", ErrorCode.PHONE_NUMBER_DUPLICATION);
        }

        return userRepository.save(makeUser(requestDto));
    }

    private User makeUser(UserSaveRequestDto requestDto) {
        return User.builder()
                .userPhone(requestDto.getPhone())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .gender(requestDto.getGender())
                .date(DateUtil.stringToDate(requestDto.getBirth()))
                .role(requestDto.getRole())
                .build();
    }
}
