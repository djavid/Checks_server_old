package com.djavid.checkserver.controller;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.entity.response.TokenResponse;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.djavid.checkserver.util.Config.ERROR_TOKEN_EMPTY;


@RestController
@RequestMapping(value = "token")
public class TokenController {

    private final RegistrationTokenRepository registrationTokenRepository;


    public TokenController(RegistrationTokenRepository registrationTokenRepository) {
        this.registrationTokenRepository = registrationTokenRepository;
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<RegistrationToken> getTokens() {
        return registrationTokenRepository.findAll();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TokenResponse getToken(@PathVariable("id") long id) {
        Optional<RegistrationToken> token = registrationTokenRepository.findById(id);
        return token.map(TokenResponse::new)
                .orElseGet(() -> new TokenResponse("No such token!"));
    }


    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse registerToken(@RequestHeader("Token") String token) {

        try {
            if (token.equals(""))
                return new BaseResponse(ERROR_TOKEN_EMPTY);

            RegistrationToken foundToken = registrationTokenRepository.findRegistrationTokenByToken(token);

            if (foundToken != null) {
                ChecksApplication.log.info("Token (%s) with id(%d) exists", foundToken.getToken(), foundToken.getId());
                return new BaseResponse(foundToken);
            } else {
                RegistrationToken newToken = new RegistrationToken(token);
                newToken.setCreated(System.currentTimeMillis());
                RegistrationToken savedToken = registrationTokenRepository.save(newToken);

                ChecksApplication.log.info("Saved token (%s) with id(%d)", savedToken.getToken(), savedToken.getId());
                return new BaseResponse(savedToken);
            }

        } catch (Exception e) {
            return new BaseResponse(e.getMessage());
        }
    }

}

