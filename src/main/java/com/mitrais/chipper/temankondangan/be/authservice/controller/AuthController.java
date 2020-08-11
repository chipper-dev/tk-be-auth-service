package com.mitrais.chipper.temankondangan.be.authservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mitrais.chipper.temankondangan.be.authservice.common.CommonResource;
import com.mitrais.chipper.temankondangan.be.authservice.common.ResponseBody;
import com.mitrais.chipper.temankondangan.be.authservice.model.LoginWrapper;
import com.mitrais.chipper.temankondangan.be.authservice.security.TokenProvider;
import com.mitrais.chipper.temankondangan.be.authservice.service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;

@Api(value = "Auth")
@RestController
@RequestMapping("/auth")
public class AuthController extends CommonResource {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    TokenProvider tokenProvider;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseBody> login(@RequestBody LoginWrapper data, HttpServletRequest request) {
        authService.login(data.getEmail(), data.getPassword());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));
        String jwt = tokenProvider.createToken(authentication);

        return ResponseEntity.ok(getResponseBody(HttpStatus.OK.value(), jwt, null));
    }

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer <access_token>")
    @GetMapping("/logout")
    public ResponseEntity<ResponseBody> logout(HttpServletRequest request) {
        String token = getToken(request.getHeader("Authorization"));
        authService.logout(tokenProvider.getUserIdFromToken(token));

        return ResponseEntity.ok(getResponseBody(HttpStatus.OK.value(), null, null));
    }
}
