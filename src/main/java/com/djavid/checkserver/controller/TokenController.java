package com.djavid.checkserver.controller;

import com.djavid.checkserver.ChecksApplication;
import com.djavid.checkserver.model.entity.RegistrationToken;
import com.djavid.checkserver.model.entity.response.BaseResponse;
import com.djavid.checkserver.model.entity.response.TokenResponse;
import com.djavid.checkserver.model.repository.RegistrationTokenRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


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
    public BaseResponse registerToken(@RequestParam("token") String device_token, @RequestParam("id") long db_id) {

        if (device_token.equals(""))
            return new BaseResponse("Wrong device id!");

        RegistrationToken registrationToken = registrationTokenRepository.findRegistrationTokenByToken(device_token);
        if (registrationToken != null && registrationToken.getToken().equals(device_token))
            return new BaseResponse(registrationToken);

        try {

            if (db_id == 0 || registrationTokenRepository.findRegistrationTokenById(db_id) == null) {

                RegistrationToken token = new RegistrationToken(device_token);
                token.setCreated(System.currentTimeMillis());
                RegistrationToken result = registrationTokenRepository.save(token);

                ChecksApplication.log.info("Saved token(" + device_token + ") with id(" + result.getId() + ")");
                return new BaseResponse(result);

            } else if (registrationTokenRepository.findRegistrationTokenById(db_id) != null) {

                RegistrationToken token = registrationTokenRepository.findRegistrationTokenById(db_id);
                token.setToken(device_token);
                RegistrationToken result = registrationTokenRepository.save(token);

                ChecksApplication.log.info("Updated token(" + device_token + ") with id(" + result.getId() + ")");
                return new BaseResponse(result);

            }

            return new BaseResponse("Something gone wrong");

        } catch (Exception e) {
            return new BaseResponse(e.getMessage());
        }
    }

}

