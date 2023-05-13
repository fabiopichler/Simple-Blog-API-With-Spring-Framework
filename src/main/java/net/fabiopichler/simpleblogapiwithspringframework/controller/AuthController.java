/*-------------------------------------------------------------------------------

Copyright (c) 2023 Fábio Pichler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

-------------------------------------------------------------------------------*/

package net.fabiopichler.simpleblogapiwithspringframework.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.fabiopichler.simpleblogapiwithspringframework.dto.*;
import net.fabiopichler.simpleblogapiwithspringframework.model.User;
import net.fabiopichler.simpleblogapiwithspringframework.security.AuthService;
import net.fabiopichler.simpleblogapiwithspringframework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public AuthUserDto getUser(@AuthenticationPrincipal User user) {
        return new AuthUserDto(user);
    }

    @PostMapping("/signin")
    @PreAuthorize("not isAuthenticated()")
    public JwtTokenDto signin(@RequestBody @Valid UserCredentialsDto credentials) {
        return authService.authenticate(credentials);
    }

    @PostMapping("/signup")
    @PreAuthorize("not isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserDto signup(@RequestBody @Valid UserCreationDto userDto) {
        return userService.create(userDto);
    }

    @PostMapping("/signout")
    public String signout(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        return authService.signout(refreshTokenDto);
    }

    @PostMapping("/refresh")
    public JwtTokenDto refresh(@RequestBody @Valid RefreshTokenDto refreshTokenDto, HttpServletRequest request) {
        return authService.refresh(refreshTokenDto, request);
    }
}
