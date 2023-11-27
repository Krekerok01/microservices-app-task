package com.specificgroup.subscription.controller;

import com.specificgroup.subscription.entity.Subscription;
import com.specificgroup.subscription.service.SubscriptionService;
import com.specificgroup.subscription.util.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "Create a subscription", description = "Creation subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Client request error(fields validation)",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Client(user) cannot subscribe to himself",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There is no such user(publisher) in the database",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{userPublisherId}")
    public ResponseEntity<Long> createSubscription(@PathVariable("userPublisherId") Long userPublisherId,
                                                   HttpServletRequest httpRequest){
        Long userSubscriberId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        return new ResponseEntity<>(subscriptionService.createSubscription
                (userSubscriberId, userPublisherId), HttpStatus.CREATED);
    }

    @Operation(summary = "Find all subscriptions", description = "Finding all subscriptions from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Only admin has access to this method",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public List<Subscription> findAllSubscriptions(){
        return subscriptionService.getAllSubscriptions();
    }

    @Operation(summary = "Find all subscriptions for a specific user", description = "Finding all subscriptions for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/subscriber")
    public List<Long> findAllSubscriptionsForSpecificUser(@RequestParam Long userSubscriberId){
        return subscriptionService.getSubscriptionsBySubscriberId(userSubscriberId);
    }

    @Operation(summary = "Delete subscription", description = "Deletion subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Client(user) can manages only his subscriptions",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There is no such subscription in the database",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<?> deleteSubscription(@PathVariable Long subscriptionId, HttpServletRequest httpRequest) {
        Long userSubscriberId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        subscriptionService.deleteSubscription(subscriptionId, userSubscriberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Long getUserIdFromTheTokenInTheHttpRequest(HttpServletRequest httpRequest) {
        return JwtUtil.getUserIdFromToken(httpRequest.getHeader(HttpHeaders.AUTHORIZATION));
    }
}