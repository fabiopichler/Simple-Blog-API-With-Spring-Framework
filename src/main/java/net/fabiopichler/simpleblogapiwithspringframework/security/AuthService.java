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

package net.fabiopichler.simpleblogapiwithspringframework.security;

import jakarta.servlet.http.HttpServletRequest;
import net.fabiopichler.simpleblogapiwithspringframework.dto.AuthUserDto;
import net.fabiopichler.simpleblogapiwithspringframework.dto.JwtTokenDto;
import net.fabiopichler.simpleblogapiwithspringframework.dto.RefreshTokenDto;
import net.fabiopichler.simpleblogapiwithspringframework.dto.UserCredentialsDto;
import net.fabiopichler.simpleblogapiwithspringframework.model.RefreshToken;
import net.fabiopichler.simpleblogapiwithspringframework.model.User;
import net.fabiopichler.simpleblogapiwithspringframework.service.RefreshTokenService;
import net.fabiopichler.simpleblogapiwithspringframework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private EncryptorUtil encryptorUtil;

    public JwtTokenDto authenticate(UserCredentialsDto credentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
            );

            AuthPrincipal principal = (AuthPrincipal) authentication.getPrincipal();
            String token = jwtUtil.generateToken(principal.getUser().getId());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getUser());

            return new JwtTokenDto(
                    token,
                    encryptorUtil.encrypt(refreshToken.getToken()),
                    new AuthUserDto(principal.getUser())
            );

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public String signout(RefreshTokenDto refreshTokenDto) {
        String refreshTokenStr = encryptorUtil.decrypt(refreshTokenDto.getRefreshToken());

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found")
        );

        refreshTokenService.delete(refreshToken);

        return "{}";
    }

    public JwtTokenDto refresh(RefreshTokenDto refreshTokenDto, HttpServletRequest request) {
        try {
            final String oldRefreshToken = encryptorUtil.decrypt(refreshTokenDto.getRefreshToken());

            final String accessToken = jwtUtil.getTokenFromRequest(request);
            final long userId = jwtUtil.getUserIdExpired(accessToken);

            RefreshToken refreshToken = refreshTokenService.findByToken(oldRefreshToken).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Token not found")
            );

            final User user = refreshToken.getUser();

            refreshTokenService.delete(refreshToken);

            if (userId != user.getId())
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error");

            String newToken = jwtUtil.generateToken(user.getId());
            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

            return new JwtTokenDto(
                    newToken,
                    encryptorUtil.encrypt(newRefreshToken.getToken()),
                    new AuthUserDto(user)
            );

        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
