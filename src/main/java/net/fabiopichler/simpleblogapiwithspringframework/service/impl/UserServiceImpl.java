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

package net.fabiopichler.simpleblogapiwithspringframework.service.impl;

import net.fabiopichler.simpleblogapiwithspringframework.dto.AuthUserDto;
import net.fabiopichler.simpleblogapiwithspringframework.dto.UserCreationDto;
import net.fabiopichler.simpleblogapiwithspringframework.exceptions.UserCreationException;
import net.fabiopichler.simpleblogapiwithspringframework.model.User;
import net.fabiopichler.simpleblogapiwithspringframework.repository.UserRepository;
import net.fabiopichler.simpleblogapiwithspringframework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
    }

    @Override
    public AuthUserDto create(UserCreationDto userDto) {
        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation()))
            throw new UserCreationException("As senhas não são iguais");

        if (repository.existsByUsername(userDto.getUsername()))
            throw new UserCreationException("O nome de usuário já existe");

        if (repository.existsByEmail(userDto.getEmail()))
            throw new UserCreationException("O e-mail já está cadastrado");

        var user = new User();

        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return new AuthUserDto(repository.save(user));
    }
}
