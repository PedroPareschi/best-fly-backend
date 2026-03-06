package com.pedropareschi.bestfly.controller;

import com.pedropareschi.bestfly.dto.request.UpdateUserRequest;
import com.pedropareschi.bestfly.dto.response.UserResponse;
import com.pedropareschi.bestfly.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "Users", description = "Management of authenticated users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private UserService userService;

    @GetMapping
    @Operation(summary = "List users", description = "Returns all registered users.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned successfully",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content)
    })
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a user by identifier.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserResponse> getUser(@Parameter(description = "User ID", required = true) @PathVariable Long id) {
        Optional<UserResponse> user = userService.getUser(id);
        return ResponseEntity.of(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates a user's data by ID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields to update the user", required = true)
            @RequestBody UpdateUserRequest request
    ) {
        Optional<UserResponse> user = userService.updateUser(id, request);
        return ResponseEntity.of(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove user", description = "Removes a user by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User removed successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> deleteUser(@Parameter(description = "User ID", required = true) @PathVariable Long id) {
        if (!userService.deleteUser(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
