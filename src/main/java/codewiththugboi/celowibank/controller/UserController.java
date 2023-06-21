package codewiththugboi.celowibank.controller;



import codewiththugboi.celowibank.dto.request.*;
import codewiththugboi.celowibank.dto.response.ApiResponse;

import codewiththugboi.celowibank.exceptions.CelowiException;

import codewiththugboi.celowibank.service.user.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "api/v1/bank")
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl service;




    @PutMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request) {
        try {
            var serviceResponse = service.deposit(request);
            ApiResponse response = new ApiResponse(true, serviceResponse);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (CelowiException e) {
            ApiResponse response = new ApiResponse(false, e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/withdrawn")
    public ResponseEntity<?> withdrawn(@RequestBody TransactionRequest request) {
        try {
            var serviceResponse = service.withdrawn(request);
            ApiResponse response = new ApiResponse(true, serviceResponse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CelowiException ex) {
            ApiResponse response = new ApiResponse(false, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionRequest request) {
        try {
            var serviceResponse = service.transfer(request);
            ApiResponse response = new ApiResponse(true, serviceResponse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CelowiException ex) {
            ApiResponse response = new ApiResponse(false, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/pin")
    public ResponseEntity<?> pin(@RequestBody PinRequest request) {
        try {
            var serviceResponse = service.pin(request);
            ApiResponse response = new ApiResponse(true, serviceResponse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CelowiException ex) {
            ApiResponse response = new ApiResponse(false, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/balance")
    public ResponseEntity<?> balance(@RequestBody BalanceRequest request) {
        try {
            var serviceResponse = service.checkBalance(request);
            ApiResponse response = new ApiResponse(true, serviceResponse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CelowiException ex) {
            ApiResponse response = new ApiResponse(false, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody DeleteRequest request) {
        try {
            var serviceResponse = service.delete(request);
            ApiResponse response = new ApiResponse(true, serviceResponse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CelowiException ex) {
            ApiResponse response = new ApiResponse(false, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/find")
    public ResponseEntity<?> find() {
        try {
            var serviceResponse = service.findAll();
            ApiResponse response = new ApiResponse(true, serviceResponse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CelowiException ex) {
            ApiResponse response = new ApiResponse(false, ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }




    }







